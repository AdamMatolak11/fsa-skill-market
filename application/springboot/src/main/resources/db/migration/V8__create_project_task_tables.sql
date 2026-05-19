CREATE TABLE IF NOT EXISTS project_tasks (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    assignee_user_id UUID NULL,
    created_by_user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_project_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_project_tasks_assignee FOREIGN KEY (assignee_user_id) REFERENCES user_profiles(id),
    CONSTRAINT fk_project_tasks_creator FOREIGN KEY (created_by_user_id) REFERENCES user_profiles(id)
);

CREATE TABLE IF NOT EXISTS task_comments (
    id UUID PRIMARY KEY,
    task_id UUID NOT NULL,
    author_user_id UUID NOT NULL,
    message VARCHAR(5000) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_task_comments_task FOREIGN KEY (task_id) REFERENCES project_tasks(id),
    CONSTRAINT fk_task_comments_author FOREIGN KEY (author_user_id) REFERENCES user_profiles(id)
);
