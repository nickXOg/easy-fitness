CREATE TABLE workout_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE cardio_workouts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id BIGINT NOT NULL,
    workout_type_id UUID REFERENCES workout_types(id),
    distance DOUBLE PRECISION,
    duration INT,
    average_heart_rate INT,
    max_heart_rate INT,
    calories_burned INT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    location VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP
);

INSERT INTO workout_types (name, description) VALUES ('Running', 'Running on a treadmill or outdoors');
INSERT INTO workout_types (name, description) VALUES ('Cycling', 'Cycling on a stationary bike or outdoors');
INSERT INTO workout_types (name, description) VALUES ('Swimming', 'Swimming laps in a pool');