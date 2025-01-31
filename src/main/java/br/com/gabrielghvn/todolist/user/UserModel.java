package br.com.gabrielghvn.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_user")
public class UserModel {
  
  @Id
  @GeneratedValue(generator = "uuid")
  private UUID id;

  // @Getter
  // @Setter

  // @Column(name = "usuario")
  @Column(unique = true)
  private String username;
  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
