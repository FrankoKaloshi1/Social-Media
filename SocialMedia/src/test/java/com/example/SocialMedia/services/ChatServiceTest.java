package com.example.SocialMedia.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.SocialMedia.Dto.ChatMessageResponseDto;
import com.example.SocialMedia.Dto.SendMessageRequest;
import com.example.SocialMedia.model.Chat;
import com.example.SocialMedia.model.Follower;
import com.example.SocialMedia.model.Image;
import com.example.SocialMedia.model.Post;
import com.example.SocialMedia.model.User;
import com.example.SocialMedia.repositories.ChatRepository;
import com.example.SocialMedia.repositories.FollowerRepository;
import com.example.SocialMedia.repositories.PostRepository;
import com.example.SocialMedia.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    private static final String TEST_SECRET = "1234567890123456";

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlockService blockService;

    @Mock
    private FollowerRepository followerRepository;

    @Mock
    private PostRepository postRepository;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(
                chatRepository,
                userRepository,
                blockService,
                followerRepository,
                postRepository,
                TEST_SECRET);
    }

    @Test
    void sendMessage_allowsPostOnlyMessages() {
        User sender = createUser(1L, "Sender", "User", "sender@example.com");
        User receiver = createUser(2L, "Receiver", "User", "receiver@example.com");
        Post sharedPost = createPost(10L, sender, List.of());
        SendMessageRequest request = new SendMessageRequest();
        request.setReceiverId(receiver.getId());
        request.setPostId(sharedPost.getId());

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(blockService.isBlocked(sender.getId(), receiver.getId())).thenReturn(false);
        when(followerRepository.findByFollowerUserIdAndFollowingUserId(sender.getId(), receiver.getId()))
                .thenReturn(Optional.of(new Follower()));
        when(postRepository.findById(sharedPost.getId())).thenReturn(Optional.of(sharedPost));

        chatService.sendMessage(sender.getId(), request);

        ArgumentCaptor<Chat> chatCaptor = ArgumentCaptor.forClass(Chat.class);
        verify(chatRepository).save(chatCaptor.capture());

        Chat savedChat = chatCaptor.getValue();
        assertThat(savedChat.getMessage()).isNull();
        assertThat(savedChat.getPostShared()).isSameAs(sharedPost);
        assertThat(savedChat.getSender()).isSameAs(sender);
        assertThat(savedChat.getReceiver()).isSameAs(receiver);
    }

    @Test
    void readMessages_returnsSharedPostDtoForPostMessages() {
        User sender = createUser(1L, "Sender", "User", "sender@example.com");
        User receiver = createUser(2L, "Receiver", "User", "receiver@example.com");
        Post sharedPost = createPost(10L, sender, List.of(createImage("https://cdn.example.com/post.png")));

        Chat sharedChat = new Chat();
        sharedChat.setId(100L);
        sharedChat.setSender(sender);
        sharedChat.setReceiver(receiver);
        sharedChat.setPostShared(sharedPost);
        sharedChat.setCreatedAt(LocalDateTime.of(2026, 3, 15, 22, 0));

        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(chatRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()))
                .thenReturn(List.of(sharedChat));
        when(chatRepository.findByReceiverIdAndSenderId(sender.getId(), receiver.getId()))
                .thenReturn(List.of());

        List<ChatMessageResponseDto> messages = chatService.readMessages(sender.getId(), receiver.getId());

        assertThat(messages).hasSize(1);
        ChatMessageResponseDto response = messages.getFirst();
        assertThat(response.getMessage()).isNull();
        assertThat(response.getPostShared()).isNotNull();
        assertThat(response.getPostShared().getId()).isEqualTo(sharedPost.getId());
        assertThat(response.getPostShared().getContent()).isEqualTo(sharedPost.getContent());
        assertThat(response.getPostShared().getImageUrls()).containsExactly("https://cdn.example.com/post.png");
    }

    private User createUser(Long id, String name, String surname, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        return user;
    }

    private Post createPost(Long id, User user, List<Image> images) {
        Post post = new Post();
        post.setId(id);
        post.setContent("Shared post content");
        post.setCreatedAt(LocalDateTime.of(2026, 3, 15, 21, 45));
        post.setUser(user);
        post.setImages(images);
        return post;
    }

    private Image createImage(String fileUrl) {
        Image image = new Image();
        image.setFileUrl(fileUrl);
        return image;
    }
}