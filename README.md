# ihub

Table roles {
    role_id varchar2 [pk]
    role_name varchar2 [not null, unique]
    description varchar2
}

Table users {
    email varchar(255) [pk]
    password varchar(255) [not null]
    name varchar(255) [not null]
    
    company varchar(255)              // 회사명
    position varchar(255)             // 직함
    phone_number varchar(50)          // 휴대전화번호
    profile_image_url varchar(500)    // 프로필 사진
    
    status varchar(20) [not null, default: 'ACTIVE']
    // 가능 값: ACTIVE, INACTIVE, LOCKED, DELETED
    
    created_at timestamptz
    updated_at timestamptz
}

Table user_roles {
    user_role_id varchar2 [pk]
    user_id varchar2 [not null]
    role_id varchar2 [not null]

    Note: "UNIQUE(user_id, role_id)"
}
