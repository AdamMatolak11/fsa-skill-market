INSERT INTO user_profiles (
    id, email, display_name, bio, role, skills_csv, average_rating, rating_count, created_at
) VALUES (
    '70c2b1c0-01d7-4f8f-a454-b0fd462ec2f7',
    'charlie@skillmarket.local',
    'Charlie Green',
    'Full stack engineer interested in remote opportunities.',
    'FREELANCER',
    'java,spring-boot,vuejs,docker',
    0.00,
    0,
    TIMESTAMP WITH TIME ZONE '2026-05-11 10:00:00+01:00'
)
ON CONFLICT (id) DO NOTHING;
