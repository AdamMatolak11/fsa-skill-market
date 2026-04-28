INSERT INTO user_profiles (
    id, email, display_name, bio, role, skills_csv, average_rating, rating_count, created_at
) VALUES
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
    );

INSERT INTO projects (
    id, title, description, budget, status, created_at
) VALUES (
    '44444444-4444-4444-4444-444444444444',
    'Completed integration cleanup',
    'Finalize API integration and deployment handover.',
    1800.00,
    'COMPLETED',
    TIMESTAMP WITH TIME ZONE '2026-01-20 10:00:00+01:00'
);
