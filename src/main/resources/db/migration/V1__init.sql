CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    task_priority    VARCHAR(30)  NOT NULL,
    task_status      VARCHAR(30)  NOT NULL DEFAULT 'NEW',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_tasks_status ON tasks (task_status);
CREATE INDEX idx_tasks_priority ON tasks (task_priority);
