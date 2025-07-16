package com.task.TaskManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data

public class LoginResponse {
//   private String email;
//       private String password;
        private String token;
        private String message;

        public String getMessage() {
                return message;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        public String getToken() {
                return token;
        }

        public void setToken(String token) {
                this.token = token;
        }


                public LoginResponse() {}

                public LoginResponse(String token, String message) {
                        this.token = token;
                        this.message = message;
                }

                // getters and setters


}
