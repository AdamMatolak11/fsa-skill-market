ALTER TABLE projects
    ADD COLUMN client_id UUID NULL,
    ADD COLUMN assigned_freelancer_id UUID NULL;

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_client FOREIGN KEY (client_id) REFERENCES user_profiles(id);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_assigned_freelancer FOREIGN KEY (assigned_freelancer_id) REFERENCES user_profiles(id);

UPDATE projects
SET client_id = '11111111-1111-1111-1111-111111111111'
WHERE id IN (
    '2b94fbc8-86bc-4d7f-b8ba-e9bb89ad4e20',
    '44444444-4444-4444-4444-444444444444'
);

UPDATE projects
SET assigned_freelancer_id = '22222222-2222-2222-2222-222222222222'
WHERE id = '44444444-4444-4444-4444-444444444444';
