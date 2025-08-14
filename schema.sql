CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  email VARCHAR(120) UNIQUE NOT NULL,
  phone VARCHAR(30),
  address TEXT,
  role VARCHAR(10) NOT NULL CHECK (role IN ('ADMIN','TRAINER','MEMBER')),
  created_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS memberships (
  membership_id SERIAL PRIMARY KEY,
  membership_type VARCHAR(50) NOT NULL,
  membership_description TEXT,
  membership_cost NUMERIC(10,2) NOT NULL CHECK (membership_cost >= 0),
  member_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  purchased_at TIMESTAMPTZ DEFAULT now()
);

CREATE TABLE IF NOT EXISTS workout_classes (
  workout_class_id SERIAL PRIMARY KEY,
  class_type VARCHAR(50) NOT NULL,
  class_description TEXT,
  trainer_id INTEGER REFERENCES users(user_id) ON DELETE SET NULL,
  scheduled_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS gym_merch (
  merch_id SERIAL PRIMARY KEY,
  merch_name VARCHAR(80) NOT NULL,
  merch_type VARCHAR(40) NOT NULL,
  merch_price NUMERIC(10,2) NOT NULL CHECK (merch_price >= 0),
  quantity_in_stock INTEGER NOT NULL CHECK (quantity_in_stock >= 0)
);

CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_workout_classes_trainer ON workout_classes(trainer_id);
CREATE INDEX IF NOT EXISTS idx_memberships_member ON memberships(member_id);
