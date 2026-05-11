INSERT INTO user_profiles (
    id, email, display_name, bio, role, skills_csv, average_rating, rating_count, created_at
) VALUES
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
    )
ON CONFLICT (id) DO NOTHING;
