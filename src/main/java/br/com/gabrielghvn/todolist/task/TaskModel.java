package br.com.gabrielghvn.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_task")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "uuid")
  private UUID id;
  private String description;

  @Column(length = 50)
  private String title;
  private LocalDateTime startAt;
  private LocalDateTime endAt;
  private String priority;

  @CreationTimestamp
  private LocalDateTime createdAt;

  private UUID idUser;

  public void setTitle(String title) throws IllegalArgumentException {
    if(title.length() > 50) {
      throw new IllegalArgumentException("Title cannot be longer than 50 characters.");
    }
    this.title = title;
  }
}
