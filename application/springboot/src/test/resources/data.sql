INSERT INTO user_profiles (
    id, email, display_name, bio, role, skills_csv, average_rating, rating_count, created_at
)
VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'client@skillmarket.local',
        'Client Demo',
        'Product owner preparing marketplace requirements.',
        'CLIENT',
        '',
        0.00,
        0,
        TIMESTAMP WITH TIME ZONE '2026-01-05 09:00:00+01:00'
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'freelancer@skillmarket.local',
        'Freelancer Demo',
        'Backend freelancer focused on Java and Spring.',
        'FREELANCER',
        'java,spring,postgresql',
        4.80,
        5,
        TIMESTAMP WITH TIME ZONE '2026-01-06 09:00:00+01:00'
    ),
    (
        '33333333-3333-3333-3333-333333333333',
        'admin@skillmarket.local',
        'Admin Demo',
        'Platform administrator.',
        'ADMIN',
        '',
        0.00,
        0,
        TIMESTAMP WITH TIME ZONE '2026-01-07 09:00:00+01:00'
    ),
    (
        '55555555-5555-5555-5555-555555555555',
        'jdoe@skillmarket.local',
        'John Doe',
        'Experienced startup founder looking for talent.',
        'CLIENT',
        '',
        0.00,
        0,
        TIMESTAMP WITH TIME ZONE '2026-02-01 10:00:00+01:00'
    ),
    (
        '66666666-6666-6666-6666-666666666666',
        'asmith@skillmarket.local',
        'Alice Smith',
        'Full-stack developer with a passion for clean code.',
        'FREELANCER',
        'javascript,typescript,react,node',
        4.95,
        12,
        TIMESTAMP WITH TIME ZONE '2026-02-02 11:30:00+01:00'
    ),
    (
        '77777777-7777-7777-7777-777777777777',
        'bbrown@skillmarket.local',
        'Bob Brown',
        'UI/UX Designer with 5 years of experience.',
        'FREELANCER',
        'figma,sketch,adobe-xd',
        4.50,
        8,
        TIMESTAMP WITH TIME ZONE '2026-02-03 14:15:00+01:00'
    ),
    (
        '88888888-8888-8888-8888-888888888888',
        'company@skillmarket.local',
        'Tech Solutions Ltd.',
        'Corporate client outsourcing development tasks.',
        'CLIENT',
        '',
        0.00,
        0,
        TIMESTAMP WITH TIME ZONE '2026-02-04 09:45:00+01:00'
    );

INSERT INTO projects (id, client_id, assigned_freelancer_id, title, description, budget, status, created_at)
VALUES
    (
        '2b94fbc8-86bc-4d7f-b8ba-e9bb89ad4e20',
        '11111111-1111-1111-1111-111111111111',
        NULL,
        'Spring Boot API',
        'Implement API endpoint for projects.',
        1500.00,
        'OPEN',
        TIMESTAMP WITH TIME ZONE '2026-01-10 10:15:30+01:00'
    ),
    (
        '44444444-4444-4444-4444-444444444444',
        '11111111-1111-1111-1111-111111111111',
        '22222222-2222-2222-2222-222222222222',
        'Completed integration cleanup',
        'Finalize API integration and deployment handover.',
        1800.00,
        'COMPLETED',
        TIMESTAMP WITH TIME ZONE '2026-01-20 10:00:00+01:00'
    );
