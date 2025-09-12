/**
 * Generic function to submit a form using Fetch API
 * Supports GET, POST, PUT, DELETE requests
 * @param {string} url - The endpoint URL
 * @param {string} formId - ID of the form
 * @param {string} method - HTTP method: GET, POST, PUT, DELETE
 * @returns {boolean} false - Prevents default form submission
 */
console.log("Attendograde dashboard loaded!");
function submitForm(url, formId, method = 'POST') {
    const form = document.getElementById(formId);
    const formData = new FormData(form);
    const params = new URLSearchParams(formData).toString();

    let fetchOptions = {
        method: method,
        headers: {}
    };

    // For POST/PUT/DELETE, send params as query string
    if (method === 'GET') {
        url += '?' + params;
    } else {
        url += '?' + params; // sending via query parameters for Spring @RequestParam
    }

    fetch(url, fetchOptions)
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            return response.text().then(text => { throw new Error(text || 'Request failed'); });
        }
    })
    .then(msg => {
        document.getElementById(formId + '-msg').innerText = '✅ ' + msg;
        form.reset();
    })
    .catch(error => {
        document.getElementById(formId + '-msg').innerText = '❌ ' + error.message;
    });

    return false; // Prevent page reload
}

/**
 * Fetch and display all students (for view-all.html)
 */
function fetchAllStudents() {
    fetch('/students/all')
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById('students-list');
            list.innerHTML = '';
            data.forEach(s => {
                const li = document.createElement('li');
                li.textContent = `ID: ${s.id}, Name: ${s.name}, Roll No: ${s.rollNo}, Email: ${s.email}`;
                list.appendChild(li);
            });
        })
        .catch(err => {
            document.getElementById('students-list').innerText = '❌ Error fetching students';
        });
}

/**
 * Fetch and display a student report card (for report-card.html)
 */
function getReportCard() {
    const id = document.getElementById('studentId').value;
    fetch(`/students/${id}/report`)
        .then(res => {
            if (res.ok) return res.json();
            else throw new Error('Student not found');
        })
        .then(data => {
            let html = '';
            for (let subject in data) {
                html += `📘 ${subject}: ${data[subject]}<br>`;
            }
            document.getElementById('report').innerHTML = html;
        })
        .catch(err => {
            document.getElementById('report').innerText = '❌ ' + err.message;
        });
}
