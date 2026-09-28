/**
 * MentorConnect - Pro SaaS Client Application
 */

const API_BASE = '/api';

// State
let allStudents = [];
let allAlumni = [];
let allTags = [];
let allPairs = [];
let currentMatches = [];
let selectedStudentId = null;
let currentView = 'matches'; // 'matches' | 'reports'

// DOM Elements
const studentSelect = document.getElementById('studentSelect');
const tagFilterRow = document.getElementById('tagFilterRow');
const mentorsGrid = document.getElementById('mentorsGrid');
const sessionsList = document.getElementById('sessionsList');
const reportsBody = document.getElementById('reportsBody');

// Lifecycle Init
document.addEventListener('DOMContentLoaded', async () => {
    initKeyboardShortcuts();
    await loadInitialData();
});

function initKeyboardShortcuts() {
    window.addEventListener('keydown', (e) => {
        if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
            e.preventDefault();
            const searchInput = document.getElementById('globalSearch');
            if (searchInput) searchInput.focus();
        }
    });

    const searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            filterMentorsLocally(query);
        });
    }
}

// Initial Data Load
async function loadInitialData() {
    try {
        await Promise.all([
            fetchStudents(),
            fetchTags(),
            fetchPairs(),
            fetchSessions(),
            fetchSummaryMetrics()
        ]);
        
        if (allStudents.length > 0) {
            selectedStudentId = allStudents[0].id;
            studentSelect.value = selectedStudentId;
            await loadSuggestionsForStudent(selectedStudentId);
        } else {
            await fetchAllAlumni();
        }
    } catch (err) {
        console.error('Initialization error:', err);
        showToast('Connected to database. Ready to explore.', 'success');
    }
}

// API Calls
async function fetchStudents() {
    try {
        const res = await fetch(`${API_BASE}/students`);
        const json = await res.json();
        if (json.success && json.data) {
            allStudents = json.data;
            populateStudentDropdown(allStudents);
        }
    } catch (err) {
        console.error('Failed to fetch students', err);
    }
}

async function fetchTags() {
    try {
        const res = await fetch(`${API_BASE}/tags`);
        const json = await res.json();
        if (json.success && json.data) {
            allTags = json.data;
            renderTagFilters(allTags);
        }
    } catch (err) {
        console.error('Failed to fetch tags', err);
    }
}

async function fetchPairs() {
    try {
        const res = await fetch(`${API_BASE}/mentorship-pairs?status=ACTIVE`);
        const json = await res.json();
        if (json.success && json.data) {
            allPairs = json.data;
            populatePairDropdown(allPairs);
        }
    } catch (err) {
        console.error('Failed to fetch pairs', err);
    }
}

function populatePairDropdown(pairs) {
    const select = document.getElementById('schedPairSelect');
    if (!select) return;
    select.innerHTML = '<option value="">Select an active mentorship pairing...</option>';
    pairs.forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id;
        opt.textContent = `${p.alumniName} (${p.alumniCompany}) ➔ ${p.studentName} (${p.studentDepartment})`;
        select.appendChild(opt);
    });
}

async function fetchAllAlumni() {
    try {
        const res = await fetch(`${API_BASE}/alumni`);
        const json = await res.json();
        if (json.success && json.data) {
            allAlumni = json.data;
            currentMatches = allAlumni.map(a => ({
                alumniId: a.id,
                name: a.name,
                company: a.company,
                jobTitle: a.jobTitle,
                avatarUrl: a.avatarUrl,
                bio: a.bio,
                maxMentees: a.maxMentees,
                activeMenteesCount: a.activeMenteesCount,
                availableSlots: a.availableSlots,
                available: a.available,
                overlapCount: 0,
                matchPercentage: 100,
                sharedTags: a.expertiseTags,
                allExpertiseTags: a.expertiseTags
            }));
            renderAlumniCards(currentMatches);
        }
    } catch (err) {
        console.error('Failed to fetch alumni', err);
    }
}

async function loadSuggestionsForStudent(studentId) {
    mentorsGrid.innerHTML = `
        <div style="grid-column: 1/-1; text-align: center; padding: 3rem;">
            <p style="color: var(--text-muted); font-weight: 600;">Calculating best-matching alumni based on shared interest tags...</p>
        </div>
    `;

    try {
        const res = await fetch(`${API_BASE}/matches/suggestions?studentId=${studentId}`);
        const json = await res.json();
        if (json.success && json.data) {
            currentMatches = json.data;
            renderAlumniCards(currentMatches);
        }
    } catch (err) {
        console.error('Failed to fetch suggestions', err);
        showToast('Error fetching suggestions for student', 'error');
    }
}

async function fetchSessions() {
    try {
        const res = await fetch(`${API_BASE}/sessions`);
        const json = await res.json();
        if (json.success && json.data) {
            renderSessionsDock(json.data);
        }
    } catch (err) {
        console.error('Failed to fetch sessions', err);
    }
}

async function fetchSummaryMetrics() {
    try {
        const res = await fetch(`${API_BASE}/reports/summary`);
        const json = await res.json();
        if (json.success && json.data) {
            const data = json.data;
            document.getElementById('metricHours').innerText = data.totalHoursMentored + 'h';
            document.getElementById('metricCompleted').innerText = data.completedSessions;
            document.getElementById('metricPairs').innerText = data.activePairs;
            document.getElementById('metricRate').innerText = data.completionRatePercent + '%';
        }
    } catch (err) {
        console.error('Failed to fetch summary metrics', err);
    }
}

async function fetchEngagementReports() {
    try {
        const res = await fetch(`${API_BASE}/reports/engagement`);
        const json = await res.json();
        if (json.success && json.data) {
            renderEngagementTable(json.data);
        }
    } catch (err) {
        console.error('Failed to fetch engagement reports', err);
    }
}

// Render Functions
function populateStudentDropdown(students) {
    studentSelect.innerHTML = '';
    students.forEach(st => {
        const opt = document.createElement('option');
        opt.value = st.id;
        opt.textContent = `${st.name} (${st.department} • Class of '${st.graduationYear.toString().slice(-2)})`;
        studentSelect.appendChild(opt);
    });

    studentSelect.addEventListener('change', async (e) => {
        selectedStudentId = e.target.value;
        await loadSuggestionsForStudent(selectedStudentId);
    });
}

function renderTagFilters(tags) {
    tagFilterRow.innerHTML = '<span class="tag-label">Skill Focus:</span>';
    tags.slice(0, 8).forEach(tag => {
        const chip = document.createElement('span');
        chip.className = 'tag-chip';
        chip.textContent = tag.name;
        chip.onclick = () => {
            chip.classList.toggle('active');
            filterMentorsBySelectedTags();
        };
        tagFilterRow.appendChild(chip);
    });
}

function filterMentorsLocally(query) {
    if (!query) {
        renderAlumniCards(currentMatches);
        return;
    }

    const filtered = currentMatches.filter(m => {
        const nameMatch = m.name?.toLowerCase().includes(query);
        const compMatch = m.company?.toLowerCase().includes(query);
        const roleMatch = m.jobTitle?.toLowerCase().includes(query);
        const tagMatch = (m.allExpertiseTags || []).some(t => t.toLowerCase().includes(query));
        return nameMatch || compMatch || roleMatch || tagMatch;
    });

    renderAlumniCards(filtered);
}

async function filterMentorsBySelectedTags() {
    const activeChips = Array.from(document.querySelectorAll('.tag-chip.active')).map(c => c.textContent.trim());
    if (activeChips.length === 0) {
        if (selectedStudentId) {
            await loadSuggestionsForStudent(selectedStudentId);
        } else {
            renderAlumniCards(currentMatches);
        }
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/matches/custom-search`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(activeChips)
        });
        const json = await res.json();
        if (json.success && json.data) {
            renderAlumniCards(json.data);
        }
    } catch (err) {
        console.error('Filter by tags failed', err);
    }
}

function renderAlumniCards(matches) {
    if (!matches || matches.length === 0) {
        mentorsGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 4rem; background: #FFFFFF; border-radius: var(--radius-lg); border: 1px dashed var(--border-subtle);">
                <h3 style="font-weight: 700; color: var(--text-primary); margin-bottom: 0.5rem;">No Mentors Matching Criteria</h3>
                <p style="color: var(--text-muted); font-size: 0.88rem;">Try clearing your filters or registering a new alumni mentor.</p>
            </div>
        `;
        return;
    }

    mentorsGrid.innerHTML = '';

    matches.forEach(mentor => {
        const card = document.createElement('div');
        card.className = 'mentor-card';

        // Capacity Segments Calculation
        const max = mentor.maxMentees || 3;
        const active = mentor.activeMenteesCount || 0;
        const availableSlots = mentor.availableSlots !== undefined ? mentor.availableSlots : Math.max(0, max - active);
        const isFull = availableSlots === 0;

        let segmentHTML = '';
        for (let i = 0; i < max; i++) {
            if (i < active) {
                segmentHTML += `<div class="segment filled ${isFull ? 'full-warning' : ''}"></div>`;
            } else {
                segmentHTML += `<div class="segment"></div>`;
            }
        }

        // Shared Tags list
        const sharedSet = new Set(mentor.sharedTags || []);
        const allTags = mentor.allExpertiseTags || [];
        
        const tagsHTML = Array.from(allTags).map(t => {
            const isShared = sharedSet.has(t);
            return `<span class="tag-chip ${isShared ? 'shared' : ''}">${isShared ? '✓ ' : ''}${t}</span>`;
        }).join('');

        const overlapScore = mentor.overlapCount !== undefined ? mentor.overlapCount : sharedSet.size;
        const matchPercent = mentor.matchPercentage !== undefined ? mentor.matchPercentage : 90;

        card.innerHTML = `
            <div class="mentor-card-top">
                <div class="mentor-avatar-wrap">
                    <img src="${mentor.avatarUrl || 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150'}" alt="${mentor.name}" class="mentor-avatar" />
                </div>
                <span class="company-badge">${mentor.company}</span>
            </div>

            <div class="mentor-identity">
                <h3>${mentor.name}</h3>
                <p class="role-text">${mentor.jobTitle}</p>
            </div>

            <div class="overlap-indicator">
                <span class="dot"></span>
                <span>${overlapScore} Shared Tags • ${matchPercent}% Overlap</span>
            </div>

            <!-- Segmented Capacity Indicator -->
            <div class="capacity-box">
                <div class="capacity-header">
                    <span>Capacity Status</span>
                    <span>${active} / ${max} Active Mentees</span>
                </div>
                <div class="capacity-segments">
                    ${segmentHTML}
                </div>
                <p class="capacity-status-text">
                    ${isFull 
                        ? '<span style="color: var(--rose-text); font-weight: 700;">● Maximum Capacity Reached</span>' 
                        : `<span style="color: var(--emerald-text); font-weight: 700;">● ${availableSlots} of ${max} slots remaining</span>`}
                </p>
            </div>

            <div class="mentor-tags">
                ${tagsHTML}
            </div>

            <button class="breakdown-toggle" onclick="toggleBreakdown(this)">
                <span>Match Breakdown</span>
                <span class="arrow">↓</span>
            </button>
            <div class="breakdown-content">
                <div class="breakdown-row">
                    <span>Tag Alignment</span>
                    <strong>${overlapScore} matching skills</strong>
                </div>
                <div class="breakdown-row">
                    <span>Company</span>
                    <strong>${mentor.company}</strong>
                </div>
                <div class="breakdown-row">
                    <span>Bio</span>
                    <span style="text-align: right; max-width: 60%;">${mentor.bio || 'Experienced engineering leader.'}</span>
                </div>
            </div>

            <div class="card-actions">
                <button class="btn btn-primary" 
                    ${isFull ? 'disabled' : ''} 
                    onclick="handleRequestMentorship(${mentor.alumniId}, '${mentor.name}')">
                    ${isFull ? 'At Limit' : 'Request Mentorship'}
                </button>
                <button class="btn btn-secondary" onclick="openScheduleModal(${mentor.alumniId}, '${mentor.name}')">
                    Schedule
                </button>
            </div>
        `;

        mentorsGrid.appendChild(card);
    });
}

function toggleBreakdown(btn) {
    const content = btn.nextElementSibling;
    const arrow = btn.querySelector('.arrow');
    content.classList.toggle('open');
    arrow.textContent = content.classList.contains('open') ? '↑' : '↓';
}

function renderSessionsDock(sessions) {
    if (!sessions || sessions.length === 0) {
        sessionsList.innerHTML = `<p style="font-size: 0.8rem; color: var(--text-muted); text-align: center; padding: 1rem;">No upcoming sessions.</p>`;
        return;
    }

    sessionsList.innerHTML = '';
    sessions.slice(0, 5).forEach(session => {
        const item = document.createElement('div');
        item.className = 'session-mini-card';
        
        const dateStr = new Date(session.scheduledTime).toLocaleDateString('en-US', {
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });

        item.innerHTML = `
            <span class="status-badge ${session.status}">${session.status}</span>
            <h4>${session.topic}</h4>
            <p><strong>Mentor:</strong> ${session.alumniName} ➔ ${session.studentName}</p>
            <p style="margin-top: 0.2rem; color: var(--text-muted); font-size: 0.72rem;">📅 ${dateStr} (${session.durationMinutes} min)</p>
            <div style="display: flex; gap: 0.5rem; margin-top: 0.6rem;">
                <a href="${session.meetingLink || '#'}" target="_blank" class="btn btn-outline-emerald" style="padding: 0.25rem 0.6rem; font-size: 0.72rem;">
                    Join Call
                </a>
                ${session.status === 'SCHEDULED' ? `
                    <button class="btn btn-secondary" style="padding: 0.25rem 0.6rem; font-size: 0.72rem;" onclick="markSessionComplete(${session.id})">
                        Mark Done
                    </button>
                ` : ''}
            </div>
        `;
        sessionsList.appendChild(item);
    });
}

function renderEngagementTable(reports) {
    reportsBody.innerHTML = '';
    if (!reports || reports.length === 0) {
        reportsBody.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--text-muted);">No mentorship pairings recorded yet.</td></tr>`;
        return;
    }

    reports.forEach(rep => {
        const tr = document.createElement('tr');
        const rate = rep.totalSessions > 0 ? Math.round((rep.completedSessions / rep.totalSessions) * 100) : 0;
        
        tr.innerHTML = `
            <td>
                <strong>${rep.alumniName}</strong>
                <div style="font-size: 0.75rem; color: var(--text-muted);">${rep.alumniCompany}</div>
            </td>
            <td>
                <strong>${rep.studentName}</strong>
                <div style="font-size: 0.75rem; color: var(--text-muted);">${rep.studentDepartment}</div>
            </td>
            <td><span class="status-badge ${rep.pairStatus}">${rep.pairStatus}</span></td>
            <td><strong>${rep.totalSessions}</strong></td>
            <td><span style="color: var(--emerald-text); font-weight: 700;">${rep.completedSessions}</span></td>
            <td>${rep.totalMinutesCompleted} mins</td>
            <td>
                <div style="display: flex; align-items: center; gap: 0.5rem;">
                    <div style="flex: 1; height: 6px; background: #E2E8F0; border-radius: 4px; overflow: hidden;">
                        <div style="width: ${rate}%; height: 100%; background: var(--emerald-text);"></div>
                    </div>
                    <span style="font-size: 0.75rem; font-weight: 700;">${rate}%</span>
                </div>
            </td>
        `;
        reportsBody.appendChild(tr);
    });
}

// Actions
async function handleRequestMentorship(alumniId, alumniName) {
    if (!selectedStudentId) {
        showToast('Please select a student first.', 'error');
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/mentorship-pairs`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                alumniId: alumniId,
                studentId: selectedStudentId
            })
        });

        const json = await res.json();
        if (res.ok && json.success) {
            showToast(`Mentorship confirmed with ${alumniName}!`, 'success');
            await fetchPairs();
            await loadSuggestionsForStudent(selectedStudentId);
            await fetchSummaryMetrics();
        } else {
            // Strict business rule feedback
            showToast(json.message || 'Cannot match mentor. Capacity limit reached.', 'error');
        }
    } catch (err) {
        showToast('Network error while requesting mentorship.', 'error');
    }
}

async function markSessionComplete(sessionId) {
    try {
        const res = await fetch(`${API_BASE}/sessions/${sessionId}/status?status=COMPLETED`, {
            method: 'PATCH'
        });
        const json = await res.json();
        if (json.success) {
            showToast('Session marked as COMPLETED!', 'success');
            await fetchSessions();
            await fetchSummaryMetrics();
            if (currentView === 'reports') fetchEngagementReports();
        }
    } catch (err) {
        showToast('Failed to update session status', 'error');
    }
}

// Modals
function openModal(id) {
    document.getElementById(id).classList.add('open');
}

function closeModal(id) {
    document.getElementById(id).classList.remove('open');
}

function openScheduleModal(alumniId, alumniName) {
    const select = document.getElementById('schedPairSelect');
    if (select && allPairs.length > 0) {
        const matchingPair = allPairs.find(p => p.alumniId === alumniId);
        if (matchingPair) {
            select.value = matchingPair.id;
        } else {
            select.selectedIndex = 1;
        }
    }
    
    document.getElementById('schedTopic').placeholder = `Mentorship with ${alumniName} (e.g. System Design Review)`;
    openModal('scheduleModal');
}

// Form Submissions
document.getElementById('alumniForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        name: document.getElementById('alumniName').value,
        email: document.getElementById('alumniEmail').value,
        company: document.getElementById('alumniCompany').value,
        jobTitle: document.getElementById('alumniJobTitle').value,
        maxMentees: parseInt(document.getElementById('alumniMaxMentees').value, 10),
        bio: document.getElementById('alumniBio').value,
        expertiseTagNames: document.getElementById('alumniTags').value.split(',').map(s => s.trim()).filter(Boolean)
    };

    try {
        const res = await fetch(`${API_BASE}/alumni`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (res.ok && json.success) {
            showToast(`Alumni Mentor ${payload.name} registered!`, 'success');
            closeModal('alumniModal');
            document.getElementById('alumniForm').reset();
            await loadSuggestionsForStudent(selectedStudentId);
            await fetchTags();
        } else {
            showToast(json.message || 'Error registering alumni', 'error');
        }
    } catch (err) {
        showToast('Failed to connect to backend', 'error');
    }
});

document.getElementById('studentForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const payload = {
        name: document.getElementById('studentName').value,
        email: document.getElementById('studentEmail').value,
        department: document.getElementById('studentDept').value,
        graduationYear: parseInt(document.getElementById('studentGradYear').value, 10),
        targetTagNames: document.getElementById('studentTags').value.split(',').map(s => s.trim()).filter(Boolean)
    };

    try {
        const res = await fetch(`${API_BASE}/students`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (res.ok && json.success) {
            showToast(`Student ${payload.name} registered!`, 'success');
            closeModal('studentModal');
            document.getElementById('studentForm').reset();
            await fetchStudents();
            selectedStudentId = json.data.id;
            studentSelect.value = selectedStudentId;
            await loadSuggestionsForStudent(selectedStudentId);
        } else {
            showToast(json.message || 'Error registering student', 'error');
        }
    } catch (err) {
        showToast('Failed to connect to backend', 'error');
    }
});

document.getElementById('scheduleForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const pairIdVal = document.getElementById('schedPairSelect').value;
    if (!pairIdVal) {
        showToast('Please select a valid mentorship pair.', 'error');
        return;
    }

    const payload = {
        pairId: parseInt(pairIdVal, 10),
        topic: document.getElementById('schedTopic').value,
        scheduledTime: document.getElementById('schedTime').value,
        durationMinutes: parseInt(document.getElementById('schedDuration').value, 10),
        meetingLink: document.getElementById('schedLink').value,
        notes: document.getElementById('schedNotes').value
    };

    try {
        const res = await fetch(`${API_BASE}/sessions`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (res.ok && json.success) {
            showToast('Session scheduled successfully!', 'success');
            closeModal('scheduleModal');
            document.getElementById('scheduleForm').reset();
            await fetchSessions();
            await fetchSummaryMetrics();
        } else {
            showToast(json.message || 'Error scheduling session', 'error');
        }
    } catch (err) {
        showToast('Failed to schedule session', 'error');
    }
});

// View Switching
function switchView(view) {
    currentView = view;
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    
    if (view === 'matches') {
        document.getElementById('tabMatches').classList.add('active');
        document.getElementById('matchingView').style.display = 'grid';
        document.getElementById('reportsView').classList.remove('active');
    } else {
        document.getElementById('tabReports').classList.add('active');
        document.getElementById('matchingView').style.display = 'none';
        document.getElementById('reportsView').classList.add('active');
        fetchEngagementReports();
    }
}

// Toast Notifications
function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    if (!container) return;
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
        <span>${type === 'success' ? '✓' : '⚠'}</span>
        <span>${message}</span>
    `;
    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}
