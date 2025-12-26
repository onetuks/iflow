package com.onetuks.ihub.service;

import com.onetuks.ihub.entity.interfaces.ChannelAdapter;
import com.onetuks.ihub.entity.interfaces.InterfaceStatus;
import com.onetuks.ihub.entity.interfaces.InterfaceType;
import com.onetuks.ihub.entity.interfaces.SyncAsyncType;
import com.onetuks.ihub.entity.project.Project;
import com.onetuks.ihub.entity.project.ProjectStatus;
import com.onetuks.ihub.entity.role.Role;
import com.onetuks.ihub.entity.system.SystemStatus;
import com.onetuks.ihub.entity.system.SystemType;
import com.onetuks.ihub.entity.task.Task;
import com.onetuks.ihub.entity.task.TaskStatus;
import com.onetuks.ihub.entity.task.TaskType;
import com.onetuks.ihub.entity.user.User;
import com.onetuks.ihub.entity.user.UserStatus;
import com.onetuks.ihub.mapper.UUIDProvider;
import com.onetuks.ihub.repository.InterfaceJpaRepository;
import com.onetuks.ihub.repository.InterfaceStatusJpaRepository;
import com.onetuks.ihub.repository.ProjectJpaRepository;
import com.onetuks.ihub.repository.RoleJpaRepository;
import com.onetuks.ihub.repository.SystemJpaRepository;
import com.onetuks.ihub.repository.TaskJpaRepository;
import com.onetuks.ihub.repository.UserJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class ServiceTestDataFactory {

  private ServiceTestDataFactory() {
  }

  public static List<Role> createRoles(RoleJpaRepository roleJpaRepository) {
    return roleJpaRepository.saveAll(
        List.of(
            new Role(
                UUIDProvider.provideUUID(Role.TABLE_NAME),
                "USER_FULL_ACCESS",
                "계정 다건조회/단건조회/생성/수정"),
            new Role(UUIDProvider.provideUUID(Role.TABLE_NAME),
                "PROJECT_PERSONAL_ACCESS",
                "프로젝트 단건조회/생성/수정/권한수정 (내 플젝만 가능), 멤버삭제"),
            new Role(UUIDProvider.provideUUID(Role.TABLE_NAME),
                "SYSTEM_PERSONAL_ACCESS",
                "시스템 다건조회/생성/수정/삭제(내 플젝만 가능)"),
            new Role(UUIDProvider.provideUUID(Role.TABLE_NAME), "TASK_FULL_ACCESS", "일감 다건조회/단건조회"),
            new Role(
                UUIDProvider.provideUUID(Role.TABLE_NAME),
                "POST_FULL_ACCESS",
                "다건조회/단건조회/생성/수정/삭제")));
  }

  public static User createUser(UserJpaRepository userJpaRepository) {
    return userJpaRepository.save(
        new User(
            UUID.randomUUID().toString().substring(0, 10) + "@naver.com",
            "pass", "나훈아", new String[]{"인스피언", "하이닉스"}[new Random().nextInt(2)],
            "선임", "010-1234-4321", "profile-img.png",
            UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now()));
  }

  public static Project createProject(
      ProjectJpaRepository projectJpaRepository, User creator, User admin, String title) {
    Project project = new Project();
    project.setProjectId(UUIDProvider.provideUUID(Project.TABLE_NAME));
    project.setTitle(title);
    project.setStatus(ProjectStatus.ACTIVE);
    project.setCreatedBy(creator);
    project.setCurrentAdmin(admin);
    project.setCreatedAt(LocalDateTime.now());
    project.setUpdatedAt(LocalDateTime.now());
    return projectJpaRepository.save(project);
  }

  public static com.onetuks.ihub.entity.system.System createSystem(
      SystemJpaRepository systemJpaRepository,
      Project project,
      User creator,
      User updater,
      String systemCode) {
    com.onetuks.ihub.entity.system.System system = new com.onetuks.ihub.entity.system.System();
    system.setSystemId(UUIDProvider.provideUUID(com.onetuks.ihub.entity.system.System.TABLE_NAME));
    system.setProject(project);
    system.setSystemCode(systemCode);
    system.setStatus(SystemStatus.ACTIVE);
    system.setDescription("desc");
    system.setSystemType(SystemType.DB);
    system.setEnvironment(com.onetuks.ihub.entity.system.SystemEnvironment.DEV);
    system.setCreatedBy(creator);
    system.setUpdatedBy(updater);
    system.setCreatedAt(LocalDateTime.now());
    system.setUpdatedAt(LocalDateTime.now());
    return systemJpaRepository.save(system);
  }

  public static InterfaceStatus createInterfaceStatus(
      InterfaceStatusJpaRepository interfaceStatusJpaRepository,
      Project project,
      String name,
      int seqOrder) {
    InterfaceStatus status = new InterfaceStatus();
    status.setStatusId(UUIDProvider.provideUUID(InterfaceStatus.TABLE_NAME));
    status.setProject(project);
    status.setName(name);
    status.setCode(name.toUpperCase());
    status.setSeqOrder(seqOrder);
    status.setIsDefault(seqOrder == 1);
    status.setCreatedAt(LocalDateTime.now());
    status.setUpdatedAt(LocalDateTime.now());
    return interfaceStatusJpaRepository.save(status);
  }

  public static com.onetuks.ihub.entity.interfaces.Interface createInterface(
      InterfaceJpaRepository interfaceJpaRepository,
      Project project,
      com.onetuks.ihub.entity.system.System sourceSystem,
      com.onetuks.ihub.entity.system.System targetSystem,
      InterfaceStatus status,
      User creator,
      String ifId) {
    com.onetuks.ihub.entity.interfaces.Interface anInterface =
        new com.onetuks.ihub.entity.interfaces.Interface();
    anInterface.setInterfaceId(UUIDProvider.provideUUID(
        com.onetuks.ihub.entity.interfaces.Interface.TABLE_NAME));
    anInterface.setProject(project);
    anInterface.setIfId(ifId);
    anInterface.setSourceSystem(sourceSystem);
    anInterface.setTargetSystem(targetSystem);
    anInterface.setModule("MOD");
    anInterface.setInterfaceType(InterfaceType.REALTIME);
    anInterface.setPattern("pattern");
    anInterface.setSenderAdapter(ChannelAdapter.HTTP);
    anInterface.setReceiverAdapter(ChannelAdapter.REST);
    anInterface.setSa(SyncAsyncType.SYNC);
    anInterface.setStatus(status);
    anInterface.setBatchTimeLabel("batch");
    anInterface.setRemark("remark");
    anInterface.setCreatedBy(creator);
    anInterface.setCreatedAt(LocalDateTime.now());
    anInterface.setUpdatedAt(LocalDateTime.now());
    return interfaceJpaRepository.save(anInterface);
  }

  public static Task createTask(
      TaskJpaRepository taskJpaRepository, Project project, User creator, String title) {
    Task task = new Task();
    task.setTaskId(UUIDProvider.provideUUID(Task.TABLE_NAME));
    task.setProject(project);
    task.setTaskType(TaskType.PARENT);
    task.setTitle(title);
    task.setStatus(TaskStatus.REQUEST);
    task.setCreatedBy(creator);
    task.setCreatedAt(LocalDateTime.now());
    task.setUpdatedAt(LocalDateTime.now());
    return taskJpaRepository.save(task);
  }
}
