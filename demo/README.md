# Synthetic demo data

This folder is isolated from the application source. It contains a clone of the
MySQL structure used by the current Spring Boot backend and a deterministic data
generator for local demos, screenshots, and development.

All people, phone numbers, email addresses, products, and business records in
this folder are synthetic. `.invalid` email domains are reserved and cannot be
registered.

## Files

| File | Purpose |
|---|---|
| `schema_clone.sql` | Clean MySQL 8 DDL for all ten tables currently mapped by the backend. |
| `generate-demo-data.js` | Dependency-free Node.js generator; no `npm install` is needed. |
| `demo_data.sql` | Generated INSERT-only dataset for the cloned schema. |

## Generated record counts

| Table | Rows | Notes |
|---|---:|---|
| `users` | 53 | 1 admin, 2 owners, 50 workers |
| `workers` | 50 | Assignment-compatible worker usernames |
| `tasks` | 100 | Two tasks per worker; Pending / In Progress / Completed |
| `production_orders` | 500 | Five progress entries per task |
| `orders` | 50 | Supporting customer orders used by owner/worker dashboards and payment references |
| `payments` | 50 | One logical payment per supporting order |
| `stocks` | 20 | Owner-managed materials only; `worker_username` is `NULL` |
| `activity_logs` | 100 | Login, task, production, order, payment, and stock activity |
| `notifications` | 0 | Table exists but was not requested |
| `reports` | 0 | Table exists but was not requested |

## Generate the SQL

Requirements: Node.js 18 or newer. The script uses only built-in Node modules.

From the project root:

```powershell
node demo/generate-demo-data.js
```

Or from this folder:

```powershell
node generate-demo-data.js
```

Output:

```text
C:\Users\HP\DT_textile_industry\demo\demo_data.sql
```

The same date and seed produce the same dataset:

```powershell
node demo/generate-demo-data.js --date=2026-09-24 --seed=20260924
```

Supported options:

- `--date=YYYY-MM-DD` — base date for due dates, deadlines, and timestamps.
- `--seed=<integer>` — deterministic pseudo-random seed.
- Environment variables `DEMO_DATE` and `DEMO_SEED` are also supported.

The generator validates row counts, primary-key uniqueness, BCrypt hashes,
allowed status/stage values, valid dates, and logical references before writing
the file. The live backend schema has no physical foreign keys, so the script
also validates the same username/order relationships explicitly.

The current `tasks` table has no quantity column, so the assigned quantity is
recorded in `task_name` as `Qty N`. The current `production_orders` table has
no `task_id` column, so each production row carries its task link as
`PO-DEMO-T<task_id>-E<entry>`. These encodings preserve the existing schema
without inventing columns.

## Import into MySQL

> **Warning:** `schema_clone.sql` drops and recreates its ten tables. Import it
> only into a new/disposable demo database, never into a production or working
> database.

### 1. Create an empty UTF-8 database

```powershell
mysql -u root -p -e "CREATE DATABASE textile_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"
```

### 2. Import the schema, then the data

Command Prompt / Git Bash:

```bat
mysql -u root -p textile_demo < demo\schema_clone.sql
mysql -u root -p textile_demo < demo\demo_data.sql
```

PowerShell, if `mysql` is not on `PATH`:

```powershell
$mysql = 'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
cmd /c "`"$mysql`" -u root -p textile_demo < `"$PWD\demo\schema_clone.sql`""
cmd /c "`"$mysql`" -u root -p textile_demo < `"$PWD\demo\demo_data.sql`""
```

Do not put a database password directly in a committed script. Pass it with
`-p` so MySQL prompts securely.

## Demo accounts

Every generated account uses the BCrypt hash for password `Demo@123`.

| Role | Username |
|---|---|
| Admin | `demo_admin` |
| Owner | `demo_owner1` |
| Owner | `demo_owner2` |
| Worker | `demo_worker001` through `demo_worker050` |

The `workers.worker_name` values intentionally use the matching login
usernames. The current worker task screens filter `tasks.assigned_worker` by the
authenticated username, while the owner production-assignment form stores a
`workers.worker_name`; using one canonical value keeps both workflows valid.
