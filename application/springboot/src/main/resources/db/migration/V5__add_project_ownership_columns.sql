DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='projects' AND column_name='client_id') THEN
        ALTER TABLE projects ADD COLUMN client_id UUID NULL;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='projects' AND column_name='assigned_freelancer_id') THEN
        ALTER TABLE projects ADD COLUMN assigned_freelancer_id UUID NULL;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name='fk_projects_client') THEN
        ALTER TABLE projects ADD CONSTRAINT fk_projects_client FOREIGN KEY (client_id) REFERENCES user_profiles(id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name='fk_projects_assigned_freelancer') THEN
        ALTER TABLE projects ADD CONSTRAINT fk_projects_assigned_freelancer FOREIGN KEY (assigned_freelancer_id) REFERENCES user_profiles(id);
    END IF;
END $$;



UPDATE projects
SET client_id = '11111111-1111-1111-1111-111111111111'
WHERE id IN (
    '2b94fbc8-86bc-4d7f-b8ba-e9bb89ad4e20',
    '44444444-4444-4444-4444-444444444444'
);

UPDATE projects
SET assigned_freelancer_id = '22222222-2222-2222-2222-222222222222'
WHERE id = '44444444-4444-4444-4444-444444444444';
