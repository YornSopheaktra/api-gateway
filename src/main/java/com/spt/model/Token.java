package com.spt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class Token implements Serializable {

    public static final String TOKEN_TYPE = "Bearer";

    protected String type;

    @JsonProperty("access_token")
    protected String accessToken;

    @JsonIgnore
    protected String tokenId;

    @JsonProperty("refresh_token")
    protected String refreshToken;

    @JsonProperty("last_login_date")
    protected Date lastLoginDate;
}
