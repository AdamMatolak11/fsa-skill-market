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

CREATE UNIQUE INDEX uk_user_profiles_email_lower ON user_profiles (LOWER(email));

CREATE TABLE offers (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES projects(id),
    freelancer_id UUID NOT NULL REFERENCES user_profiles(id),
    amount NUMERIC(19, 2) NOT NULL,
    message VARCHAR(5000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES projects(id),
    client_id UUID NOT NULL REFERENCES user_profiles(id),
    freelancer_id UUID NOT NULL REFERENCES user_profiles(id),
    score INTEGER NOT NULL,
    comment VARCHAR(5000) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
