CREATE TABLE IF NOT EXISTS tasks (
    id UUID PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    due_date DATE,
    priority VARCHAR(10) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    tags TEXT
);

CREATE TABLE IF NOT EXISTS task_attachments (
    id BIGSERIAL PRIMARY KEY,
    task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100),
    size BIGINT NOT NULL,
    uploaded_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_task_attachments_task_id ON task_attachments(task_id);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);