'use strict';

const fs = require('node:fs');
const path = require('node:path');

const C = {
    users: ['user_id', 'email', 'full_name', 'password', 'role', 'username', 'address', 'enabled', 'phone', 'reset_token', 'reset_token_expiry', 'approved'],
    workers: ['worker_id', 'availability', 'department', 'phone_number', 'skill', 'worker_name'],
    tasks: ['task_id', 'assigned_worker', 'department', 'due_date', 'status', 'task_name'],
    production_orders: ['order_id', 'deadline', 'order_number', 'product_name', 'quantity', 'status', 'assigned_worker', 'current_stage'],
    orders: ['id', 'expected_delivery_date', 'owner_username', 'product_name', 'quantity', 'remarks', 'request_date', 'status', 'worker_response', 'worker_username'],
    payments: ['payment_id', 'amount', 'order_id', 'owner_username', 'payment_date', 'payment_status', 'remarks', 'worker_username'],
    stocks: ['id', 'category', 'location', 'product_name', 'quantity', 'worker_username'],
    activity_logs: ['log_id', 'action', 'details', 'performed_by', 'timestamp']
};
const N = { users: 53, workers: 50, tasks: 100, production_orders: 500, orders: 50, payments: 50, stocks: 20, activity_logs: 100 };
const HASH = '$2a$10$YLldGPwvVDbvQlehfOoQpOoBwTY57GBZq.EbCByqN5W/lFxNSqmAu';
const TS = ['Pending', 'In Progress', 'Completed'];
const OS = ['PENDING', 'Accepted', 'IN PROGRESS', 'Completed', 'Rejected'];
const PS = ['Paid', 'Pending'];
const ST = ['Cutting', 'Stitching', 'Dyeing', 'Finishing', 'Packing', 'Completed'];
const D = ['Cutting', 'Stitching', 'Checking', 'Packing'];
const G = ['Cotton Shirt', 'Formal Pant', 'Cotton T-Shirt', 'Saree Blouse', 'School Uniform', 'Cotton Kurti', 'Night Dress', 'Checked Chudidhar', 'Denim Jeans', 'Kids Frock', 'Cotton Lungi', 'Bath Towel', 'Office Shirt', 'Track Pant', 'Party Kurta', 'Apron', 'Chef Coat', 'Medical Uniform', 'Safety Jacket', 'Workwear Set'];
const M = [
    ['Cotton Fabric', 'Fabric', 'Fabric Store A'], ['Polyester Fabric', 'Fabric', 'Fabric Store B'], ['Silk Fabric', 'Fabric', 'Premium Store'], ['Linen Fabric', 'Fabric', 'Fabric Store A'], ['Denim Fabric', 'Fabric', 'Fabric Store B'],
    ['Cotton Thread', 'Consumable', 'Thread Godown'], ['Polyester Thread', 'Consumable', 'Thread Godown'], ['Buttons', 'Accessory', 'Accessory Store'], ['Zippers', 'Accessory', 'Accessory Store'], ['Labels', 'Accessory', 'Accessory Store'],
    ['Elastic Bands', 'Accessory', 'Accessory Store'], ['Interlining', 'Consumable', 'Cutting Room'], ['Packing Bags', 'Packaging', 'Packing Store'], ['Cartons', 'Packaging', 'Dispatch Store'], ['Pattern Paper', 'Consumable', 'Cutting Room'],
    ['Needles', 'Consumable', 'Machine Store'], ['Bobbins', 'Consumable', 'Machine Store'], ['Lace Trim', 'Accessory', 'Accessory Store'], ['Dye Chemicals', 'Consumable', 'Dyeing Unit'], ['Finished Goods', 'Finished Goods', 'Dispatch Store']
];
const P = [
    'Aarav Krishnan', 'Meena Ramesh', 'Vishnu Prasad', 'Kavya Lakshmi', 'Arun Kumar', 'Divya Suresh', 'Karthik Raman', 'Anjali Selvam', 'Mohan Das', 'Sneha Iyer',
    'Ravi Shankar', 'Priya Nandini', 'Ajith Babu', 'Deepa Mohan', 'Suresh Pandian', 'Nila Rajan', 'Ganesh Murthy', 'Lakshmi Priya', 'Murali Kannan', 'Anitha Bhavani',
    'Naveen Sekar', 'Pavithra Devi', 'Saravanan Mani', 'Keerthana Sri', 'Balaji Prasad', 'Nithya Shree', 'Harish Chandran', 'Snehalatha Devi', 'Gopi Krishna', 'Revathi Raj',
    'Sathish Kumar', 'Maheswari Bai', 'Ramesh Chandran', 'Dhanalakshmi R', 'Venkat Ramu', 'Aravind Pandi', 'Sasikala Devi', 'Kumaran Subbu', 'Sandhya Rani', 'Vignesh Murthi',
    'Thenmozhi K', 'Boopathy J', 'Kausalya Devi', 'Manikandan R', 'Jayanthi S', 'Sathish Pandian', 'Revika S', 'Prakash V', 'Anitha Kumari', 'Srinivasan M'
];

function option(name, fallback) {
    const prefix = `--${name}=`;
    const item = process.argv.find((value) => value.startsWith(prefix));
    return item ? item.slice(prefix.length) : fallback;
}

function parseDate(value) {
    const match = /^(\d{4})-(\d{2})-(\d{2})$/.exec(value);
    if (!match) throw new Error(`Invalid date: ${value}`);
    const year = Number(match[1]);
    const month = Number(match[2]);
    const day = Number(match[3]);
    const result = new Date(Date.UTC(year, month - 1, day));
    if (result.getUTCFullYear() !== year || result.getUTCMonth() !== month - 1 || result.getUTCDate() !== day) throw new Error(`Invalid calendar date: ${value}`);
    return result;
}

function dateText(value) { return value.toISOString().slice(0, 10); }
function addDays(value, days) { const result = new Date(value); result.setUTCDate(result.getUTCDate() + days); return result; }
function timeText(value) { return value.toISOString().slice(0, 23).replace('T', ' '); }
function randomFactory(seed) {
    let state = seed >>> 0;
    return () => { state += 0x6D2B79F5; let x = state; x = Math.imul(x ^ (x >>> 15), x | 1); x ^= x + Math.imul(x ^ (x >>> 7), x | 61); return ((x ^ (x >>> 14)) >>> 0) / 4294967296; };
}
function choose(random, values) { return values[Math.floor(random() * values.length)]; }
function between(random, low, high) { return low + Math.floor(random() * (high - low + 1)); }

function sql(value) {
    if (value === null || value === undefined) return 'NULL';
    if (typeof value === 'number') { if (!Number.isFinite(value)) throw new Error(`Invalid number: ${value}`); return String(value); }
    if (typeof value === 'boolean') return value ? "b'1'" : "b'0'";
    return `'${String(value).replace(/\\/g, '\\\\').replace(/'/g, "''").replace(/\0/g, '\\0').replace(/\n/g, '\\n').replace(/\r/g, '\\r').replace(/\x1a/g, '\\Z')}'`;
}

function insert(table, rows, chunkSize = 100) {
    const columns = C[table];
    if (!columns || rows.length === 0) return '';
    const header = `INSERT INTO \`${table}\` (${columns.map((column) => `\`${column}\``).join(', ')}) VALUES\n`;
    const chunks = [];
    for (let start = 0; start < rows.length; start += chunkSize) {
        chunks.push(header + rows.slice(start, start + chunkSize).map((row) => `  (${columns.map((column) => sql(row[column])).join(', ')})`).join(',\n') + ';\n');
    }
    return chunks.join('\n');
}


function makeUsers() {
    const rows = [
        { user_id: 900001, email: 'admin@trizen.invalid', full_name: 'System Administrator', password: HASH, role: 'ADMIN', username: 'demo_admin', address: 'Demo Administration Office', enabled: 1, phone: '+91 90000 00001', reset_token: null, reset_token_expiry: null, approved: 1 },
        { user_id: 900002, email: 'owner1@trizen.invalid', full_name: 'Demo Owner One', password: HASH, role: 'OWNER', username: 'demo_owner1', address: 'Demo Owner Unit 1', enabled: 1, phone: '+91 90000 00002', reset_token: null, reset_token_expiry: null, approved: 1 },
        { user_id: 900003, email: 'owner2@trizen.invalid', full_name: 'Demo Owner Two', password: HASH, role: 'OWNER', username: 'demo_owner2', address: 'Demo Owner Unit 2', enabled: 1, phone: '+91 90000 00003', reset_token: null, reset_token_expiry: null, approved: 1 }
    ];
    for (let index = 0; index < 50; index += 1) {
        const number = String(index + 1).padStart(3, '0');
        rows.push({ user_id: 900004 + index, email: `worker${number}@trizen.invalid`, full_name: P[index], password: HASH, role: 'WORKER', username: `demo_worker${number}`, address: `Demo Worker Unit ${index + 1}`, enabled: 1, phone: `+91 90001 ${String(index + 1).padStart(5, '0')}`, reset_token: null, reset_token_expiry: null, approved: 1 });
    }
    return rows;
}

function makeWorkers(random) {
    const skills = { Cutting: ['Pattern Cutting', 'Straight Cutting', 'Marker Making'], Stitching: ['Machine Stitching', 'Overlock Stitching', 'Buttonhole Stitching'], Checking: ['Quality Inspection', 'Measurement Checking', 'Final Inspection'], Packing: ['Packing and Labelling', 'Carton Packing', 'Bundle Preparation'] };
    return P.map((name, index) => {
        const department = D[index % D.length];
        const number = String(index + 1).padStart(3, '0');
        return { worker_id: 900001 + index, availability: random() < 0.78 ? 'Available' : 'Busy', department, phone_number: `+91 90001 ${String(index + 1).padStart(5, '0')}`, skill: choose(random, skills[department]), worker_name: `demo_worker${number}` };
    });
}

function makeTasks(baseDate) {
    const rows = [];
    let id = 900001;
    for (let worker = 0; worker < 50; worker += 1) {
        const username = `demo_worker${String(worker + 1).padStart(3, '0')}`;
        const department = D[worker % D.length];
        for (let batch = 0; batch < 2; batch += 1) {
            const status = TS[(worker + batch) % TS.length];
            const quantity = 20 + ((worker * 17 + batch * 29) % 481);
            const offset = status === 'Completed' ? -(5 + ((worker + batch) % 21)) : status === 'In Progress' ? 2 + ((worker * 3 + batch) % 11) : 6 + ((worker + batch * 5) % 16);
            const garment = G[(worker * 2 + batch) % G.length];
            rows.push({ task_id: id, assigned_worker: username, department, due_date: dateText(addDays(baseDate, offset)), status, task_name: `${garment} - Batch ${String(batch + 1).padStart(2, '0')} (Qty ${quantity})`, _assignedQuantity: quantity });
            id += 1;
        }
    }
    return rows;
}


function splitEven(total, parts) {
    const base = Math.floor(total / parts);
    let remainder = total - base * parts;
    return Array.from({ length: parts }, () => base + (remainder-- > 0 ? 1 : 0));
}

function makeProduction(taskRows) {
    const rows = [];
    let id = 900001;
    for (const task of taskRows) {
        const target = task.status === 'Completed' ? task._assignedQuantity : task.status === 'In Progress' ? Math.max(5, Math.floor(task._assignedQuantity * 0.7)) : 0;
        const quantities = task.status === 'Pending' ? [0, 0, 0, 0, 0] : splitEven(target, 5);
        let cumulative = 0;
        quantities.forEach((quantity, index) => {
            cumulative += quantity;
            const ratio = target === 0 ? 0 : cumulative / target;
            rows.push({ order_id: id, deadline: task.due_date, order_number: `PO-DEMO-T${task.task_id}-E${index + 1}`, product_name: task.task_name.split(' - Batch ')[0], quantity, status: task.status, assigned_worker: task.assigned_worker, current_stage: task.status === 'Completed' ? 'Completed' : task.status === 'Pending' ? null : ST[Math.min(4, Math.floor(ratio * 5))] });
            id += 1;
        });
    }
    return rows;
}

function makeOrders(baseDate, random) {
    return Array.from({ length: 50 }, (_, index) => {
        const status = OS[index % OS.length];
        const requestOffset = -(45 - (index % 25));
        const deliveryOffset = status === 'Completed' || status === 'Rejected' ? -(1 + (index % 12)) : 5 + (index % 25);
        const owner = index % 2 === 0 ? 'demo_owner1' : 'demo_owner2';
        const worker = `demo_worker${String((index % 50) + 1).padStart(3, '0')}`;
        let response = null;
        if (status === 'Completed') response = 'Work completed and quality checked.';
        if (status === 'Rejected') response = 'Request declined for the current production schedule.';
        if (status === 'Accepted') response = 'Order accepted and queued for production.';
        return { id: 900001 + index, expected_delivery_date: dateText(addDays(baseDate, deliveryOffset)), owner_username: owner, product_name: G[(index * 3) % G.length], quantity: 40 + between(random, 0, 460), remarks: `Synthetic ${status.toLowerCase()} customer order.`, request_date: dateText(addDays(baseDate, requestOffset)), status, worker_response: response, worker_username: worker };
    });
}

function makePayments(orderRows) {
    return orderRows.map((order, index) => {
        const status = index % 5 === 0 ? 'Pending' : 'Paid';
        const amount = Number(((order.quantity * (18 + (index % 9))) + 250 + (index % 7) * 35).toFixed(2));
        return { payment_id: 900001 + index, amount, order_id: order.id, owner_username: order.owner_username, payment_date: status === 'Paid' ? dateText(addDays(parseDate(order.request_date), 7 + (index % 10))) : null, payment_status: status, remarks: status === 'Paid' ? 'Synthetic settled order payment.' : 'Synthetic payment awaiting settlement.', worker_username: order.worker_username };
    });
}

function makeStocks(random) {
    return M.map(([product, category, location], index) => ({ id: 900001 + index, category, location, product_name: product, quantity: 120 + between(random, 0, 1880), worker_username: null }));
}


function makeLogs(baseDate, random, taskRows, productionRows, orderRows, paymentRows, stockRows) {
    const actions = ['LOGIN', 'TASK_STATUS', 'PRODUCTION_STATUS', 'ORDER_STATUS', 'PAYMENT_STATUS', 'STOCK_VIEW'];
    return Array.from({ length: 100 }, (_, index) => {
        const action = actions[index % actions.length];
        let performer = choose(random, ['demo_admin', 'demo_owner1', 'demo_owner2', `demo_worker${String(between(random, 1, 50)).padStart(3, '0')}`]);
        let details;
        if (action === 'TASK_STATUS') { const row = taskRows[(index * 7) % taskRows.length]; performer = row.assigned_worker; details = `Task ${row.task_id} status recorded as ${row.status}.`; }
        else if (action === 'PRODUCTION_STATUS') { const row = productionRows[(index * 5) % productionRows.length]; performer = row.assigned_worker; details = `Production entry ${row.order_number} recorded ${row.quantity} units.`; }
        else if (action === 'ORDER_STATUS') { const row = orderRows[(index * 3) % orderRows.length]; performer = row.owner_username; details = `Order ${row.id} is currently ${row.status}.`; }
        else if (action === 'PAYMENT_STATUS') { const row = paymentRows[(index * 2) % paymentRows.length]; performer = row.owner_username; details = `Payment ${row.payment_id} is ${row.payment_status}.`; }
        else if (action === 'STOCK_VIEW') { performer = index % 2 === 0 ? 'demo_owner1' : 'demo_owner2'; const row = stockRows[(index * 3) % stockRows.length]; details = `Owner reviewed ${row.product_name}; available quantity ${row.quantity}.`; }
        else details = 'Synthetic user signed in successfully.';
        return { log_id: 900001 + index, action, details, performed_by: performer, timestamp: timeText(addDays(baseDate, -(index % 30))) };
    });
}

function assertUnique(rows, key, label) {
    const values = new Set();
    for (const row of rows) { if (values.has(row[key])) throw new Error(`Duplicate ${label}: ${row[key]}`); values.add(row[key]); }
}

function validate(data) {
    for (const [table, count] of Object.entries(N)) if (data[table].length !== count) throw new Error(`${table}: expected ${count}, got ${data[table].length}`);
    assertUnique(data.users, 'user_id', 'user id'); assertUnique(data.users, 'username', 'username'); assertUnique(data.workers, 'worker_id', 'worker id'); assertUnique(data.tasks, 'task_id', 'task id');
    assertUnique(data.production_orders, 'order_id', 'production order id'); assertUnique(data.production_orders, 'order_number', 'production order number'); assertUnique(data.orders, 'id', 'order id'); assertUnique(data.payments, 'payment_id', 'payment id'); assertUnique(data.stocks, 'id', 'stock id'); assertUnique(data.activity_logs, 'log_id', 'activity log id');
    const usernames = new Set(data.users.map((row) => row.username));
    const workerNames = new Set(data.workers.map((row) => row.worker_name));
    const orderIds = new Set(data.orders.map((row) => row.id));
    const taskIds = new Set(data.tasks.map((row) => row.task_id));
    if (data.users.some((row) => !/^\$2[aby]\$\d{2}\$[./A-Za-z0-9]{53}$/.test(row.password))) throw new Error('Invalid BCrypt hash');
    for (const row of data.tasks) if (!usernames.has(row.assigned_worker) || !workerNames.has(row.assigned_worker) || !TS.includes(row.status)) throw new Error(`Invalid task ${row.task_id}`);
    for (const row of data.production_orders) { const match = /^PO-DEMO-T(\d+)-E\d+$/.exec(row.order_number); if (!match || !taskIds.has(Number(match[1])) || !usernames.has(row.assigned_worker) || !workerNames.has(row.assigned_worker) || !TS.includes(row.status) || (row.current_stage !== null && !ST.includes(row.current_stage))) throw new Error(`Invalid production ${row.order_id}`); }
    for (const row of data.orders) if (!usernames.has(row.owner_username) || !usernames.has(row.worker_username) || !OS.includes(row.status)) throw new Error(`Invalid order ${row.id}`);
    for (const row of data.payments) if (!orderIds.has(row.order_id) || !usernames.has(row.owner_username) || !usernames.has(row.worker_username) || !PS.includes(row.payment_status)) throw new Error(`Invalid payment ${row.payment_id}`);
    if (data.stocks.some((row) => row.worker_username !== null)) throw new Error('Stocks must be owner-managed');
}


function main() {
    const baseDate = parseDate(option('date', process.env.DEMO_DATE || dateText(new Date())));
    const seedText = option('seed', process.env.DEMO_SEED || '20260924');
    const seed = Number(seedText);
    if (!Number.isInteger(seed)) throw new Error(`Invalid seed: ${seedText}`);
    const random = randomFactory(seed);
    const data = { users: makeUsers(), workers: makeWorkers(random), tasks: makeTasks(baseDate), production_orders: [], orders: makeOrders(baseDate, random), payments: [], stocks: makeStocks(random), activity_logs: [] };
    data.production_orders = makeProduction(data.tasks);
    data.payments = makePayments(data.orders);
    data.activity_logs = makeLogs(baseDate, random, data.tasks, data.production_orders, data.orders, data.payments, data.stocks);
    validate(data);
    const header = ['-- Smart Textile Tracking System synthetic demo data', '-- Generated by generate-demo-data.js; all records are fictional.', `-- Base date: ${dateText(baseDate)}; seed: ${seed}`, '-- Demo password for all accounts: Demo@123 (stored as BCrypt).', '', 'SET NAMES utf8mb4;', 'START TRANSACTION;', ''].join('\n');
    const sections = Object.keys(C).map((table) => `-- ${table}: ${data[table].length} rows\n${insert(table, data[table])}`);
    const output = `${header}${sections.join('\n')}\nCOMMIT;\n`;
    const outputPath = path.join(__dirname, 'demo_data.sql');
    fs.writeFileSync(outputPath, output, 'utf8');
    console.log(`Created: ${outputPath}`);
    for (const table of Object.keys(C)) console.log(`${table}: ${data[table].length}`);
}

main();

