CREATE TABLE IF NOT EXISTS projects (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    budget NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

INSERT INTO projects (id, title, description, budget, status, created_at)
VALUES
    ('2b94fbc8-86bc-4d7f-b8ba-e9bb89ad4e20', 'Spring Boot API', 'Implement API endpoint for projects.', 1500.00, 'OPEN', NOW())
ON CONFLICT (id) DO NOTHING;
