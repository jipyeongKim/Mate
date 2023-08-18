package com.mate.kosmo.command.users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialDTO {
    private String access_token;
    private String refresh_token;
    private String token_type;
}// SocialDTO
