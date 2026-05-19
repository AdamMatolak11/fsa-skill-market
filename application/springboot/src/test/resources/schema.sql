CREATE TABLE projects (
    id UUID PRIMARY KEY,
    client_id UUID,
    assigned_freelancer_id UUID,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    budget NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE user_profiles (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    bio VARCHAR(5000) NOT NULL,
    role VARCHAR(20) NOT NULL,
    skills_csv VARCHAR(2000) NOT NULL,
    average_rating NUMERIC(3, 2) NOT NULL DEFAULT 0,
    rating_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE offers (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    freelancer_id UUID NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    message VARCHAR(5000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_offers_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_offers_freelancer FOREIGN KEY (freelancer_id) REFERENCES user_profiles(id)
);

CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    client_id UUID NOT NULL,
    freelancer_id UUID NOT NULL,
    score INTEGER NOT NULL,
    comment VARCHAR(5000) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_ratings_project FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_ratings_client FOREIGN KEY (client_id) REFERENCES user_profiles(id),
    CONSTRAINT fk_ratings_freelancer FOREIGN KEY (freelancer_id) REFERENCES user_profiles(id)
);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_client FOREIGN KEY (client_id) REFERENCES user_profiles(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_assigned_freelancer FOREIGN KEY (assigned_freelancer_id) REFERENCES user_profiles(id);

CREATE TABLE project_tasks (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL,
    assignee_user_id UUID,
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

CREATE TABLE task_comments (
    id UUID PRIMARY KEY,
    task_id UUID NOT NULL,
    author_user_id UUID NOT NULL,
    message VARCHAR(5000) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_task_comments_task FOREIGN KEY (task_id) REFERENCES project_tasks(id),
    CONSTRAINT fk_task_comments_author FOREIGN KEY (author_user_id) REFERENCES user_profiles(id)
);
