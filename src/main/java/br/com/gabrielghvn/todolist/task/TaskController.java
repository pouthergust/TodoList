package br.com.gabrielghvn.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabrielghvn.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;
  
  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    taskModel.setIdUser((UUID) idUser);

    var currentDate = LocalDateTime.now();
    var startDate = taskModel.getStartAt();

    if (currentDate.isAfter(startDate)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A Data de incio deve ser maior do que a data atual");
    }

    if (startDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("A Data de termino deve ser maior do que a Data de incio");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(HttpStatus.OK).body(task);
  }

  @GetMapping("/")
  public List<TaskModel> list(HttpServletRequest request) {
    var idUser = request.getAttribute("idUser");
    return this.taskRepository.findByIdUser((UUID) idUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
    var idUser = request.getAttribute("idUser");
    var task = this.taskRepository.findById(id).orElse(null);

    if (task == null ||!task.getIdUser().equals(idUser)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Usuario não tem permissão para alterar a tarefa");
    }

    Utils.copyNonNullProperties(taskModel, task);
    taskModel.setIdUser((UUID) idUser);
    taskModel.setId(id);
    return ResponseEntity.status(200).body(this.taskRepository.save(task));
  }
}
