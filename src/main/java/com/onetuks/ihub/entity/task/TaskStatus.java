package com.onetuks.ihub.entity.task;

public enum TaskStatus {
  REQUEST,
  IN_PROGRESS,
  COMPLETED,
  CLOSED, // 아예 작업이 완료된 것
  CANCELED, // 작업이 취소된 것
  DELETED // 잘못된 작업 등록을 취소하는 것
}
