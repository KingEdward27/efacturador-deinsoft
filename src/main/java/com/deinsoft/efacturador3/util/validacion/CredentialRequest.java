/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.validacion;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author user
 */
public class CredentialRequest {
    
    
    @JsonProperty("grant_type") 
    private String grantType;
    
    @JsonProperty("client_id") 
    private String clientId;
    
    @JsonProperty("client_secret") 
    private String clientSecret;

    public CredentialRequest() {
    }

    public CredentialRequest(String grantType, String clientId, String clientSecret) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    
}
