const API_BASE = '/api/v1';

const state = {
    genres: [],
    students: [],
    books: [],
    borrows: [],
    page: 'dashboard'
};

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('search-input').addEventListener('input', filterCurrentTable);
    document.querySelector('.search-btn').addEventListener('click', filterCurrentTable);
    document.getElementById('add-new-btn').addEventListener('click', openContextAction);
    document.addEventListener('keydown', event => {
        if (event.key === 'Escape') closeModal();
    });
    loadAllData();
});

async function request(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, options);
    if (!response.ok) {
        const detail = await response.text();
        throw new Error(detail || `Request failed (${response.status})`);
    }
    if (response.status === 204 || response.headers.get('content-length') === '0') return null;
    const contentType = response.headers.get('content-type') || '';
    return contentType.includes('application/json') ? response.json() : null;
}

async function loadAllData() {
    const results = await Promise.allSettled([
        request('/genre'), request('/student'), request('/book'), request('/borrowBook')
    ]);
    const keys = ['genres', 'students', 'books', 'borrows'];
    results.forEach((result, index) => {
        if (result.status === 'fulfilled') state[keys[index]] = result.value || [];
        else console.error(`Unable to load ${keys[index]}:`, result.reason);
    });
    renderAll();
    if (results.some(result => result.status === 'rejected')) {
        showNotification('Some library data could not be loaded', 'error');
    }
}

function renderAll() {
    renderGenres();
    renderStudents();
    renderBooks();
    renderBorrowRecords();
    renderDashboard();
}

function showPage(page) {
    if (window.event) window.event.preventDefault();
    const target = document.getElementById(`${page}-page`);
    if (!target) return false;
    state.page = page;
    document.querySelectorAll('.page').forEach(item => item.classList.remove('active'));
    target.classList.add('active');
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.toggle('active', item.getAttribute('onclick')?.includes(`'${page}'`));
    });
    const title = page === 'dashboard' ? 'LMS Dashboard' : `${capitalize(page)} Management`;
    document.querySelector('.header h1').textContent = title;
    document.getElementById('search-input').value = '';
    document.getElementById('add-new-btn').hidden = page === 'dashboard' || page === 'settings';
    return false;
}

function openContextAction() {
    const actions = {
        students: openAddStudent,
        books: openAddBook,
        genres: openAddGenre,
        borrow: openIssuePage
    };
    actions[state.page]?.();
}

function renderDashboard() {
    const today = new Date().toISOString().slice(0, 10);
    document.getElementById('total-students').textContent = state.students.length;
    document.getElementById('total-books').textContent = state.books.length;
    document.getElementById('active-borrowings').textContent = state.borrows.filter(item => !item.returnDate).length;
    document.getElementById('returned-today').textContent = state.borrows.filter(item => item.returnDate === today).length;

    const tbody = document.getElementById('activity-tbody');
    const records = [...state.borrows].sort((a, b) => b.id - a.id).slice(0, 6);
    tbody.innerHTML = records.length ? records.map(record => `
        <tr>
            <td>${escapeHtml(record.book?.bookName || 'Unknown book')}</td>
            <td>${escapeHtml(studentName(record.student))}</td>
            <td>${formatDate(record.borrowDate)}</td>
            <td><span class="${record.returnDate ? 'status-returned' : 'status-active'}">${record.returnDate ? 'Returned' : 'Active'}</span></td>
            <td>${record.returnDate ? '' : `<button class="btn btn-success btn-small" onclick="returnBook(${record.id})">Return</button>`}</td>
        </tr>`).join('') : emptyRow(5, 'No recent borrowing activity');
}

function renderStudents() {
    const tbody = document.getElementById('students-tbody');
    tbody.innerHTML = state.students.length ? state.students.map(student => `
        <tr>
            <td>${student.id}</td>
            <td>${escapeHtml(studentName(student))}</td>
            <td>${escapeHtml(student.email)}</td>
            <td>${student.age}</td>
            <td class="action-links">
                <button class="btn btn-warning btn-small" onclick="openEditStudent(${student.id})">Edit</button>
                <button class="btn btn-danger btn-small" onclick="deleteStudent(${student.id})">Delete</button>
            </td>
        </tr>`).join('') : emptyRow(5, 'No students found');
}

function renderBooks() {
    const tbody = document.getElementById('books-tbody');
    tbody.innerHTML = state.books.length ? state.books.map(book => `
        <tr>
            <td>${book.id}</td>
            <td>${escapeHtml(book.bookName)}</td>
            <td>${escapeHtml(book.author)}</td>
            <td>${escapeHtml(book.isbn)}</td>
            <td>${escapeHtml(book.genre?.name || 'Uncategorized')}</td>
            <td class="action-links">
                <button class="btn btn-warning btn-small" onclick="openEditBook(${book.id})">Edit</button>
                <button class="btn btn-danger btn-small" onclick="deleteBook(${book.id})">Delete</button>
            </td>
        </tr>`).join('') : emptyRow(6, 'No books found');
}

function renderGenres() {
    const tbody = document.getElementById('genres-tbody');
    tbody.innerHTML = state.genres.length ? state.genres.map(genre => `
        <tr>
            <td>${genre.id}</td>
            <td>${escapeHtml(genre.name)}</td>
            <td class="action-links">
                <button class="btn btn-warning btn-small" onclick="openEditGenre(${genre.id})">Edit</button>
                <button class="btn btn-danger btn-small" onclick="deleteGenre(${genre.id})">Delete</button>
            </td>
        </tr>`).join('') : emptyRow(3, 'No genres found');
}

function renderBorrowRecords() {
    const tbody = document.getElementById('borrow-tbody');
    tbody.innerHTML = state.borrows.length ? state.borrows.map(record => `
        <tr>
            <td>${record.id}</td>
            <td>${escapeHtml(studentName(record.student))}</td>
            <td>${escapeHtml(record.book?.bookName || 'Unknown book')}</td>
            <td>${formatDate(record.borrowDate)}</td>
            <td>${record.returnDate ? formatDate(record.returnDate) : '—'}</td>
            <td><span class="badge ${record.returnDate ? 'badge-returned' : 'badge-active'}">${record.returnDate ? 'Returned' : 'Active'}</span></td>
            <td>${record.returnDate ? '' : `<button class="btn btn-success btn-small" onclick="returnBook(${record.id})">Return</button>`}</td>
        </tr>`).join('') : emptyRow(7, 'No borrow records found');
}

function openAddStudent() {
    openFormModal('Add Student', `
        ${input('student-first-name', 'First name')}
        ${input('student-last-name', 'Last name')}
        ${input('student-email', 'Email', 'email')}
        ${input('student-age', 'Age', 'number', 'min="1" max="150"')}
    `, async () => {
        await mutate('/student', 'POST', {
            firstName: value('student-first-name'), lastName: value('student-last-name'),
            email: value('student-email'), age: Number(value('student-age'))
        }, 'Student added');
    });
}

function openEditStudent(id) {
    const student = state.students.find(item => item.id === id);
    if (!student) return;
    openFormModal('Edit Student', `
        ${input('student-first-name', 'First name', 'text', '', student.firstName)}
        ${input('student-last-name', 'Last name', 'text', '', student.lastName)}
        ${input('student-email', 'Email', 'email', '', student.email)}
        ${input('student-age', 'Age', 'number', 'min="1" max="150"', student.age)}
    `, async () => {
        await mutate(`/student/${id}`, 'PUT', {
            firstName: value('student-first-name'), lastName: value('student-last-name'),
            email: value('student-email'), age: Number(value('student-age'))
        }, 'Student updated');
    });
}

function openAddBook() { openBookModal(); }
function openEditBook(id) { openBookModal(state.books.find(item => item.id === id)); }

function openBookModal(book = null) {
    if (!state.genres.length) {
        showNotification('Add a genre before adding a book', 'warning');
        showPage('genres');
        return;
    }
    const genreOptions = state.genres.map(genre =>
        `<option value="${genre.id}" ${book?.genre?.id === genre.id ? 'selected' : ''}>${escapeHtml(genre.name)}</option>`
    ).join('');
    openFormModal(book ? 'Edit Book' : 'Add Book', `
        ${input('book-name', 'Book name', 'text', '', book?.bookName || '')}
        ${input('book-author', 'Author', 'text', '', book?.author || '')}
        ${input('book-isbn', 'ISBN', 'text', '', book?.isbn || '')}
        <div class="form-group"><label for="book-genre">Genre</label><select id="book-genre" required>${genreOptions}</select></div>
    `, async () => {
        const genre = state.genres.find(item => item.id === Number(value('book-genre')));
        await mutate(`/book${book ? `/${book.id}` : ''}`, book ? 'PUT' : 'POST', {
            bookName: value('book-name'), author: value('book-author'), isbn: value('book-isbn'), genre
        }, book ? 'Book updated' : 'Book added');
    });
}

function openAddGenre() { openGenreModal(); }
function openEditGenre(id) { openGenreModal(state.genres.find(item => item.id === id)); }

function openGenreModal(genre = null) {
    openFormModal(genre ? 'Edit Genre' : 'Add Genre', input('genre-name', 'Genre name', 'text', '', genre?.name || ''), async () => {
        await mutate(`/genre${genre ? `/${genre.id}` : ''}`, genre ? 'PUT' : 'POST', { name: value('genre-name') }, genre ? 'Genre updated' : 'Genre added');
    });
}

function openIssueBook() { openIssuePage(); }
function openIssuePage() {
    if (!state.students.length || !state.books.length) {
        showNotification('Add at least one student and book first', 'warning');
        return;
    }
    const students = state.students.map(item => `<option value="${item.id}">${escapeHtml(studentName(item))}</option>`).join('');
    const books = state.books.map(item => `<option value="${item.id}">${escapeHtml(item.bookName)}</option>`).join('');
    openFormModal('Issue Book', `
        <div class="form-group"><label for="borrow-student">Student</label><select id="borrow-student" required>${students}</select></div>
        <div class="form-group"><label for="borrow-book">Book</label><select id="borrow-book" required>${books}</select></div>
    `, async () => {
        const query = `?studentId=${encodeURIComponent(value('borrow-student'))}&bookId=${encodeURIComponent(value('borrow-book'))}`;
        await mutate(`/borrowBook${query}`, 'POST', null, 'Book issued');
    });
}

function openReturnBook() {
    showPage('borrow');
    showNotification('Choose an active borrowing to return', 'info');
}

async function returnBook(id) {
    if (!confirm('Mark this book as returned?')) return;
    await mutate(`/borrowBook/returnbook/${id}`, 'PUT', null, 'Book returned');
}

async function deleteStudent(id) { await remove(`/student/${id}`, 'student'); }
async function deleteBook(id) { await remove(`/book/${id}`, 'book'); }
async function deleteGenre(id) { await remove(`/genre/${id}`, 'genre'); }

async function remove(path, label) {
    if (!confirm(`Delete this ${label}?`)) return;
    await mutate(path, 'DELETE', null, `${capitalize(label)} deleted`);
}

async function mutate(path, method, body, successMessage) {
    try {
        const options = { method };
        if (body) {
            options.headers = { 'Content-Type': 'application/json' };
            options.body = JSON.stringify(body);
        }
        await request(path, options);
        closeModal();
        await loadAllData();
        showNotification(successMessage, 'success');
    } catch (error) {
        console.error(error);
        showNotification(error.message || 'The operation failed', 'error');
    }
}

function openFormModal(title, fields, submitHandler) {
    const modalBody = document.getElementById('modal-body');
    modalBody.innerHTML = `
        <h3 id="modal-title">${escapeHtml(title)}</h3>
        <form id="modal-form" class="form">${fields}
            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Save</button>
                <button class="btn btn-secondary" type="button" onclick="closeModal()">Cancel</button>
            </div>
        </form>`;
    document.getElementById('modal-form').addEventListener('submit', async event => {
        event.preventDefault();
        const submit = event.currentTarget.querySelector('[type="submit"]');
        submit.disabled = true;
        try { await submitHandler(); } finally { submit.disabled = false; }
    });
    const modal = document.getElementById('modal');
    modal.classList.remove('hidden');
    modal.classList.add('show');
    modalBody.querySelector('input, select')?.focus();
}

function closeModal() {
    const modal = document.getElementById('modal');
    modal.classList.remove('show');
    modal.classList.add('hidden');
}

function filterCurrentTable() {
    const term = document.getElementById('search-input').value.trim().toLowerCase();
    const page = document.querySelector('.page.active');
    page?.querySelectorAll('tbody tr').forEach(row => {
        row.hidden = term && !row.textContent.toLowerCase().includes(term);
    });
}

function showNotification(message, type = 'info') {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${type} show`;
    clearTimeout(showNotification.timer);
    showNotification.timer = setTimeout(() => notification.classList.remove('show'), 3500);
}

function input(id, label, type = 'text', attributes = '', currentValue = '') {
    return `<div class="form-group"><label for="${id}">${escapeHtml(label)}</label><input id="${id}" type="${type}" value="${escapeHtml(currentValue)}" ${attributes} required></div>`;
}

function value(id) { return document.getElementById(id).value.trim(); }
function studentName(student) { return student ? `${student.firstName || ''} ${student.lastName || ''}`.trim() : 'Unknown student'; }
function emptyRow(columns, message) { return `<tr><td colspan="${columns}" class="empty-state">${escapeHtml(message)}</td></tr>`; }
function capitalize(value) { return value.charAt(0).toUpperCase() + value.slice(1); }
function formatDate(date) {
    if (!date) return '—';
    return new Date(`${date}T00:00:00`).toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
}
function escapeHtml(value) {
    return String(value ?? '').replace(/[&<>"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[character]));
}

window.addEventListener('click', event => {
    if (event.target === document.getElementById('modal')) closeModal();
});
