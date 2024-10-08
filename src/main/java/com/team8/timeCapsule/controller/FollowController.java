package com.team8.timeCapsule.controller;

import com.team8.timeCapsule.domain.Follow;
import com.team8.timeCapsule.dto.FriendProfileResponse;
import com.team8.timeCapsule.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.team8.timeCapsule.security.TokenProvider;

@RestController
@RequestMapping("/api/v1/friends")
public class FollowController {

    private final TokenProvider tokenProvider;

    @Autowired
    private FriendService friendService;

    public FollowController(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @GetMapping("/list/{id}") // URL 경로 변수로 변경
    public ResponseEntity<FriendProfileResponse> getFriendList(@PathVariable String id) {
        try {
            FriendProfileResponse response = friendService.getFriendList(id);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    @GetMapping("/list")
    public ResponseEntity<FriendProfileResponse> getFriendList(HttpServletRequest request) {
        String token = tokenProvider.getTokenFromRequest(request);
        String id = tokenProvider.validateAndGetUserId(token);

        try {
            FriendProfileResponse response = friendService.getFriendList(id);
            return ResponseEntity.ok(response); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // 친구 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<Follow> sendFriendRequest(@RequestParam String senderId, @RequestParam String receiverId) {
        try {
            Follow follow = friendService.sendFriendRequest(senderId, receiverId);
            return ResponseEntity.ok(follow); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // 친구 요청 수락하기
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(@RequestParam String senderId, @RequestParam String receiverId) {
        try {
            friendService.acceptFriendRequest(senderId, receiverId);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // 친구 요청 거절하기
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectFriendRequest(@RequestParam String senderId, @RequestParam String receiverId) {
        try {
            friendService.rejectFriendRequest(senderId, receiverId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }

    // 친구 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriend(@RequestParam String senderId, @RequestParam String receiverId) {
        try {
            friendService.deleteFriend(senderId, receiverId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }
}