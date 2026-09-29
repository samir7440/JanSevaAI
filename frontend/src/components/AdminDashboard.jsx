import React, { useEffect, useMemo, useState } from "react";
import axios from "axios";

import {
  LayoutDashboard,
  MapPinned,
  Building2,
  Users,
  AlertTriangle,
  CheckCircle2,
  Clock3,
  TrendingUp,
  Activity,
  ShieldCheck,
  Search,
  RefreshCw,
  ChevronRight,
  BarChart3,
  Globe2,
  UserRound,
  CircleDot,
  Zap,
  FileWarning,
  X
} from "lucide-react";

import "./AdminDashboard.css";

/*
 * ============================================================
 * DEMO DATA
 * ============================================================
 */

const DEMO_COMPLAINTS = [
  {
    complaintId: "JS-WTR-2026-1001",
    mainCategory: "Water",
    subCategory: "Pipeline Leakage",
    status: "PENDING",
    priority: "HIGH",
    currentLevel: "VILLAGE",
    assignedPersonName: "Rajesh Verma",
    assignedPersonContact: "9876501001",
    districtOfficerName: "Vikram Patel",
    stateOfficerName: "Arun Sharma",
    createdAt: "25 Aug 2026, 09:20 AM"
  },
  {
    complaintId: "JS-ELC-2026-1002",
    mainCategory: "Electricity",
    subCategory: "Power Cut",
    status: "IN_PROGRESS",
    priority: "NORMAL",
    currentLevel: "DISTRICT",
    assignedPersonName: "Rohit Singh",
    assignedPersonContact: "9876502002",
    districtOfficerName: "Vikram Patel",
    stateOfficerName: "Arun Sharma",
    createdAt: "25 Aug 2026, 10:15 AM"
  },
  {
    complaintId: "JS-RDS-2026-1003",
    mainCategory: "Roads",
    subCategory: "Potholes",
    status: "RESOLVED",
    priority: "NORMAL",
    currentLevel: "STATE",
    assignedPersonName: "Kunal Mishra",
    assignedPersonContact: "9876503003",
    districtOfficerName: "Vikram Patel",
    stateOfficerName: "Arun Sharma",
    createdAt: "24 Aug 2026, 04:40 PM"
  },
  {
    complaintId: "JS-HLT-2026-1004",
    mainCategory: "Health",
    subCategory: "Medicine Shortage",
    status: "PENDING",
    priority: "HIGH",
    currentLevel: "DISTRICT",
    assignedPersonName: "Dr Neha Jain",
    assignedPersonContact: "9876504002",
    districtOfficerName: "Vikram Patel",
    stateOfficerName: "Arun Sharma",
    createdAt: "25 Aug 2026, 11:30 AM"
  },
  {
    complaintId: "JS-PLC-2026-1005",
    mainCategory: "Police",
    subCategory: "Harassment",
    status: "ESCALATED",
    priority: "CRITICAL",
    currentLevel: "STATE",
    assignedPersonName: "Aditya Choudhary",
    assignedPersonContact: "7089083424",
    districtOfficerName: "Vikram Patel",
    stateOfficerName: "Arun Sharma",
    createdAt: "23 Aug 2026, 08:10 PM"
  },
  {
    complaintId: "JS-GRB-2026-1006",
    mainCategory: "Garbage",
    subCategory: "Garbage Collection",
    status: "IN_PROGRESS",
    priority: "NORMAL",
    currentLevel: "VILLAGE",
    assignedPersonName: "General Officer",
    assignedPersonContact: "7440521040",
    districtOfficerName: "Vikram Patel",
    stateOfficerName: "Arun Sharma",
    createdAt: "25 Aug 2026, 12:05 PM"
  }
];

const DEMO_STATS = {
  totalComplaints: 1264,
  pending: 428,
  resolved: 716,
  escalatedCount: 120,
  villageLevel: 492,
  districtLevel: 481,
  stateLevel: 291
};

const DEMO_DEPARTMENTS = [
  ["Water", "💧", 184],
  ["Electricity", "⚡", 163],
  ["Roads", "🛣️", 142],
  ["Health", "🏥", 126],
  ["Police", "🛡️", 118],
  ["Garbage", "♻️", 96]
];

/*
 * ============================================================
 * API
 * ============================================================
 *
 * Spring Boot backend:
 * http://localhost:9090
 */

const API_BASE = "http://localhost:9090/api";

const API = {
  complaints: `${API_BASE}/admin/all`,
  stats: `${API_BASE}/admin/stats`
};

export default function AdminDashboard() {
  const [activeView, setActiveView] = useState("CENTRAL");

  const [stats, setStats] = useState(DEMO_STATS);

  const [complaints, setComplaints] =
    useState(DEMO_COMPLAINTS);

  const [search, setSearch] = useState("");

  const [loading, setLoading] =
    useState(false);

  const [selectedComplaint, setSelectedComplaint] =
    useState(null);

  const [backendOnline, setBackendOnline] =
    useState(false);

  const [usingRealData, setUsingRealData] =
    useState(false);

  /*
   * ============================================================
   * LOAD DASHBOARD DATA
   * ============================================================
   */

  const loadDashboard = async () => {
    setLoading(true);

    try {
      console.log(
        "Loading complaints from:",
        API.complaints
      );

      const complaintsResponse =
        await axios.get(
          API.complaints,
          {
            timeout: 10000
          }
        );

      const realComplaints =
        complaintsResponse.data;

      console.log(
        "REAL COMPLAINTS:",
        realComplaints
      );

      if (
        !Array.isArray(
          realComplaints
        )
      ) {
        throw new Error(
          "Invalid complaints response"
        );
      }

      /*
       * Add hierarchy display information
       * without changing backend data.
       */

      const hierarchyComplaints =
        realComplaints.map(
          (c, index) => ({
            ...c,

            districtOfficerName:
              c?.districtOfficerName ||
              c?.districtOfficer ||
              "Vikram Patel",

            stateOfficerName:
              c?.stateOfficerName ||
              c?.stateOfficer ||
              "Arun Sharma",

            hierarchyDemoId:
              index
          })
        );

      /*
       * REAL DATA -> DASHBOARD
       */

      setComplaints(
        hierarchyComplaints
      );

      /*
       * Calculate statistics from
       * actual MongoDB complaints.
       */

      const calculatedStats =
        calculateStats(
          hierarchyComplaints
        );

      setStats(
        calculatedStats
      );

      setBackendOnline(true);

      setUsingRealData(true);

    } catch (error) {

      console.error(
        "Dashboard API ERROR:",
        error
      );

      /*
       * Backend unavailable:
       * keep demo data so UI does not break.
       */

      setBackendOnline(false);

      setUsingRealData(false);

      setStats(
        DEMO_STATS
      );

      setComplaints(
        DEMO_COMPLAINTS
      );

    } finally {

      setLoading(false);

    }
  };

  /*
   * ============================================================
   * INITIAL LOAD
   * ============================================================
   */

  useEffect(() => {
    loadDashboard();
  }, []);

  /*
   * ============================================================
   * LEVEL FILTERING
   * ============================================================
   */

  const viewComplaints = useMemo(() => {

    let data = complaints;

    if (
      activeView === "STATE"
    ) {
      data =
        complaints.filter(
          c =>
            [
              "STATE",
              "DISTRICT",
              "VILLAGE"
            ].includes(
              normalizeLevel(
                c.currentLevel
              )
            )
        );
    }

    if (
      activeView === "DISTRICT"
    ) {
      data =
        complaints.filter(
          c =>
            [
              "DISTRICT",
              "VILLAGE"
            ].includes(
              normalizeLevel(
                c.currentLevel
              )
            )
        );
    }

    if (
      activeView === "OFFICER"
    ) {
      data =
        complaints.filter(
          c => {

            const name =
              String(
                c?.assignedPersonName ||
                ""
              ).trim();

            return (
              name &&
              name !==
                "NOT_ASSIGNED"
            );
          }
        );
    }

    return data;

  }, [
    complaints,
    activeView
  ]);

  /*
   * ============================================================
   * VIEW STATS
   * ============================================================
   */

  const viewStats =
    useMemo(() => {

      if (!usingRealData) {
        return calculateStats(
          viewComplaints,
          stats
        );
      }

      return calculateStats(
        viewComplaints
      );

    }, [
      viewComplaints,
      stats,
      usingRealData
    ]);

  /*
   * ============================================================
   * SEARCH
   * ============================================================
   */

  const filteredComplaints =
    useMemo(() => {

      const q =
        search
          .toLowerCase()
          .trim();

      if (!q) {
        return viewComplaints;
      }

      return viewComplaints.filter(
        c =>
          [
            c?.complaintId,
            c?.mainCategory,
            c?.subCategory,
            c?.status,
            c?.priority,
            c?.currentLevel,
            c?.assignedPersonName,
            c?.assignedPersonContact,
            c?.district,
            c?.state,
            c?.city,
            c?.village,
            c?.ward
          ]
            .filter(
              value =>
                value !== null &&
                value !== undefined
            )
            .map(
              value =>
                String(value)
            )
            .join(" ")
            .toLowerCase()
            .includes(q)
      );

    }, [
      viewComplaints,
      search
    ]);

  /*
   * ============================================================
   * DEPARTMENT INTELLIGENCE
   * ============================================================
   *
   * IMPORTANT:
   * Real dashboard departments are calculated
   * from actual complaints.
   */

  const departments =
    useMemo(() => {

      if (!usingRealData) {
        return DEMO_DEPARTMENTS;
      }

      const counts = {};

      viewComplaints.forEach(
        c => {

          const category =
            String(
              c?.mainCategory ||
              "Other"
            ).trim();

          counts[category] =
            (
              counts[category] ||
              0
            ) + 1;

        }
      );

      const icons = {
        Water: "💧",
        Electricity: "⚡",
        Roads: "🛣️",
        Health: "🏥",
        Police: "🛡️",
        Garbage: "♻️",
        Drainage: "🌊",
        Education: "🎓",
        Agriculture: "🌾",
        Transport: "🚌",
        Housing: "🏠",
        Internet: "🌐"
      };

      return Object.entries(
        counts
      )
        .sort(
          (a, b) =>
            b[1] - a[1]
        )
        .slice(0, 6)
        .map(
          ([name, value]) => [
            name,
            icons[name] ||
              "📋",
            value
          ]
        );

    }, [
      viewComplaints,
      usingRealData
    ]);

  /*
   * ============================================================
   * RESOLUTION RATE
   * ============================================================
   */

  const resolutionRate =
    viewStats.totalComplaints >
    0
      ? Math.round(
          (
            viewStats.resolved /
            viewStats.totalComplaints
          ) * 100
        )
      : 0;

  /*
   * ============================================================
   * NAVIGATION
   * ============================================================
   */

  const navItems = [
    {
      id: "CENTRAL",
      label: "Central",
      icon: Globe2
    },
    {
      id: "STATE",
      label: "State",
      icon: Building2
    },
    {
      id: "DISTRICT",
      label: "District",
      icon: MapPinned
    },
    {
      id: "OFFICER",
      label: "Officers",
      icon: Users
    }
  ];

  return (
    <div className="admin-shell">

      {/* SIDEBAR */}

      <aside className="admin-sidebar">

        <div className="brand">

          <div className="brand-icon">
            <ShieldCheck size={24} />
          </div>

          <div>
            <strong>
              JanSeva AI
            </strong>

            <span>
              Governance OS
            </span>
          </div>

        </div>

        <div className="sidebar-label">
          COMMAND CENTER
        </div>

        <nav>

          {navItems.map(
            item => {

              const Icon =
                item.icon;

              return (
                <button
                  key={
                    item.id
                  }
                  className={
                    activeView ===
                    item.id
                      ? "nav-item active"
                      : "nav-item"
                  }
                  onClick={() =>
                    setActiveView(
                      item.id
                    )
                  }
                >

                  <Icon size={18} />

                  <span>
                    {item.label}
                  </span>

                  {activeView ===
                    item.id && (
                    <ChevronRight
                      size={15}
                      className="nav-arrow"
                    />
                  )}

                </button>
              );

            }
          )}

        </nav>

        <div className="sidebar-bottom">

          <div className="system-card">

            <div className="system-top">

              <Activity size={16} />

              <span>
                SYSTEM STATUS
              </span>

            </div>

            <div className="system-status">

              <span
                className={
                  backendOnline
                    ? "status-dot online"
                    : "status-dot demo"
                }
              />

              {backendOnline
                ? usingRealData
                  ? "Backend Connected • Real Data"
                  : "Backend Connected"
                : "Demo Monitoring Mode"}

            </div>

          </div>

          <div className="india">

            🇮🇳

            <span>
              Digital Governance
            </span>

          </div>

        </div>

      </aside>

      {/* MAIN */}

      <main className="admin-main">

        {/* HEADER */}

        <header className="dashboard-header">

          <div>

            <div className="breadcrumb">

              INDIA

              <ChevronRight size={13} />

              {activeView}

              <ChevronRight size={13} />

              COMMAND CENTER

            </div>

            <h1>

              {activeView ===
              "CENTRAL"
                ? "Central Governance Dashboard"
                : activeView ===
                  "STATE"
                ? "State Operations Dashboard"
                : activeView ===
                  "DISTRICT"
                ? "District Monitoring Dashboard"
                : "Officer Operations Dashboard"}

            </h1>

            <p>
              Real-time public grievance
              monitoring and governance
              intelligence
            </p>

          </div>

          <div className="header-actions">

            <div className="live-pill">

              <span />

              {usingRealData
                ? "LIVE SYSTEM"
                : "DEMO SYSTEM"}

            </div>

            <button
              className="refresh-btn"
              onClick={
                loadDashboard
              }
              disabled={loading}
            >

              <RefreshCw
                size={16}
                className={
                  loading
                    ? "spin"
                    : ""
                }
              />

              Refresh

            </button>

          </div>

        </header>

        {/* KPI CARDS */}

        <section className="kpi-grid">

          <KpiCard
            icon={
              <FileWarning />
            }
            title="Total Complaints"
            value={
              viewStats.totalComplaints
            }
            subtitle="+12.8% this month"
            type="blue"
          />

          <KpiCard
            icon={
              <Clock3 />
            }
            title="Pending"
            value={
              viewStats.pending
            }
            subtitle="Requires action"
            type="orange"
          />

          <KpiCard
            icon={
              <CheckCircle2 />
            }
            title="Resolved"
            value={
              viewStats.resolved
            }
            subtitle={`${resolutionRate}% resolution rate`}
            type="green"
          />

          <KpiCard
            icon={
              <AlertTriangle />
            }
            title="Escalated"
            value={
              viewStats.escalatedCount
            }
            subtitle="Needs attention"
            type="red"
          />

        </section>

        {/* SECOND ROW */}

        <section className="analytics-grid">

          {/* RESOLUTION */}

          <div className="panel resolution-panel">

            <div className="panel-header">

              <div>

                <h2>
                  Resolution Performance
                </h2>

                <span>
                  Overall governance
                  efficiency
                </span>

              </div>

              <TrendingUp size={20} />

            </div>

            <div className="resolution-content">

              <div
                className="donut"
                style={{
                  "--progress":
                    `${
                      Math.min(
                        resolutionRate,
                        100
                      ) *
                      3.6
                    }deg`
                }}
              >

                <div className="donut-inner">

                  <strong>
                    {resolutionRate}%
                  </strong>

                  <span>
                    Resolved
                  </span>

                </div>

              </div>

              <div className="resolution-list">

                <MetricRow
                  label="Resolved"
                  value={
                    viewStats.resolved
                  }
                  total={
                    viewStats.totalComplaints
                  }
                  className="green-bar"
                />

                <MetricRow
                  label="Pending"
                  value={
                    viewStats.pending
                  }
                  total={
                    viewStats.totalComplaints
                  }
                  className="orange-bar"
                />

                <MetricRow
                  label="Escalated"
                  value={
                    viewStats.escalatedCount
                  }
                  total={
                    viewStats.totalComplaints
                  }
                  className="red-bar"
                />

              </div>

            </div>

          </div>

          {/* LEVEL PIPELINE */}

          <div className="panel">

            <div className="panel-header">

              <div>

                <h2>
                  Governance Pipeline
                </h2>

                <span>
                  Complaint distribution
                  by level
                </span>

              </div>

              <BarChart3 size={20} />

            </div>

            <div className="pipeline">

              <LevelBox
                icon={<Globe2 />}
                label="CENTRAL / STATE"
                value={
                  viewStats.stateLevel
                }
                percent={safePercent(
                  viewStats.stateLevel,
                  viewStats.totalComplaints
                )}
              />

              <div className="pipeline-arrow">
                →
              </div>

              <LevelBox
                icon={<Building2 />}
                label="DISTRICT"
                value={
                  viewStats.districtLevel
                }
                percent={safePercent(
                  viewStats.districtLevel,
                  viewStats.totalComplaints
                )}
              />

              <div className="pipeline-arrow">
                →
              </div>

              <LevelBox
                icon={<MapPinned />}
                label="VILLAGE"
                value={
                  viewStats.villageLevel
                }
                percent={safePercent(
                  viewStats.villageLevel,
                  viewStats.totalComplaints
                )}
              />

            </div>

          </div>

        </section>

        {/* DEPARTMENT + ACTIVITY */}

        <section className="middle-grid">

          <div className="panel">

            <div className="panel-header">

              <div>

                <h2>
                  Department Intelligence
                </h2>

                <span>
                  Complaint concentration
                </span>

              </div>

              <Zap size={19} />

            </div>

            <div className="department-list">

              {departments.length >
              0 ? (
                departments.map(
                  (
                    [
                      name,
                      icon,
                      value
                    ]
                  ) => {

                    const maxValue =
                      Math.max(
                        ...departments.map(
                          item =>
                            Number(
                              item[2]
                            ) || 0
                        ),
                        1
                      );

                    const percentage =
                      Math.round(
                        (
                          Number(
                            value
                          ) /
                          maxValue
                        ) *
                          100
                      );

                    return (
                      <div
                        className="department-row"
                        key={name}
                      >

                        <div className="department-name">

                          <span className="dept-icon">
                            {icon}
                          </span>

                          <span>
                            {name}
                          </span>

                        </div>

                        <div className="dept-bar">

                          <span
                            style={{
                              width:
                                `${percentage}%`
                            }}
                          />

                        </div>

                        <strong>
                          {value}
                        </strong>

                      </div>
                    );

                  }
                )
              ) : (
                <div className="empty">
                  No department data
                  available.
                </div>
              )}

            </div>

          </div>

          <div className="panel activity-panel">

            <div className="panel-header">

              <div>

                <h2>
                  Live Activity
                </h2>

                <span>
                  Latest governance
                  events
                </span>

              </div>

              <div className="live-small">
                LIVE
              </div>

            </div>

            <div className="activity-list">

              {getActivityItems(
                viewComplaints
              ).map(
                (
                  item,
                  index
                ) => (
                  <ActivityItem
                    key={index}
                    icon={item.icon}
                    title={
                      item.title
                    }
                    text={
                      item.text
                    }
                    time={
                      item.time
                    }
                  />
                )
              )}

            </div>

          </div>

        </section>

        {/* COMPLAINT TABLE */}

        <section className="panel complaints-panel">

          <div className="table-header">

            <div>

              <h2>
                Complaint Operations
              </h2>

              <span>
                {
                  filteredComplaints.length
                } records visible
              </span>

            </div>

            <div className="search-box">

              <Search size={17} />

              <input
                placeholder="Search complaint, department, officer..."
                value={search}
                onChange={e =>
                  setSearch(
                    e.target.value
                  )
                }
              />

            </div>

          </div>

          <div className="table-wrap">

            <table
              className={
                `complaint-table ${activeView.toLowerCase()}-view`
              }
            >

              <thead>

                <tr>

                  <th>
                    COMPLAINT
                  </th>

                  <th>
                    DEPARTMENT
                  </th>

                  <th>
                    PRIORITY
                  </th>

                  <th>
                    STATUS
                  </th>

                  <th>
                    LEVEL
                  </th>

                  {(activeView ===
                    "DISTRICT" ||
                    activeView ===
                      "STATE" ||
                    activeView ===
                      "CENTRAL") && (
                    <th>
                      ASSIGNED OFFICER
                    </th>
                  )}

                  {(activeView ===
                    "DISTRICT" ||
                    activeView ===
                      "STATE" ||
                    activeView ===
                      "CENTRAL") && (
                    <th>
                      DISTRICT OFFICER
                    </th>
                  )}

                  {(activeView ===
                    "STATE" ||
                    activeView ===
                      "CENTRAL") && (
                    <th>
                      STATE OFFICER
                    </th>
                  )}

                  <th>
                    ACTION
                  </th>

                  <th>
                    TIME
                  </th>

                </tr>

              </thead>

              <tbody>

                {filteredComplaints.map(
                  complaint => (

                    <tr
                      key={
                        complaint.complaintId ||
                        complaint._id
                      }
                      onClick={() =>
                        setSelectedComplaint(
                          complaint
                        )
                      }
                    >

                      <td>

                        <div className="complaint-id">

                          {complaint.complaintId ||
                            complaint._id ||
                            "—"}

                        </div>

                        <small>

                          {complaint.subCategory ||
                            complaint.description ||
                            "—"}

                        </small>

                      </td>

                      <td>

                        <div className="category-cell">

                          <span>
                            {getDepartmentIcon(
                              complaint.mainCategory
                            )}
                          </span>

                          {complaint.mainCategory ||
                            "Other"}

                        </div>

                      </td>

                      <td>

                        <PriorityBadge
                          value={
                            complaint.priority
                          }
                        />

                      </td>

                      <td>

                        <StatusBadge
                          value={
                            complaint.status
                          }
                        />

                      </td>

                      <td>

                        <span className="level-badge">

                          {complaint.currentLevel ||
                            "—"}

                        </span>

                      </td>

                      {(activeView ===
                        "DISTRICT" ||
                        activeView ===
                          "STATE" ||
                        activeView ===
                          "CENTRAL") && (

                        <td>

                          <div className="officer-cell">

                            <div className="officer-avatar">

                              <UserRound
                                size={15}
                              />

                            </div>

                            <div>

                              <strong>

                                {complaint.assignedPersonName ||
                                  "Not Assigned"}

                              </strong>

                              <small>

                                {complaint.assignedPersonContact ||
                                  "—"}

                              </small>

                            </div>

                          </div>

                        </td>
                      )}

                      {(activeView ===
                        "DISTRICT" ||
                        activeView ===
                          "STATE" ||
                        activeView ===
                          "CENTRAL") && (

                        <td>

                          <div className="hierarchy-officer">

                            <UserRound
                              size={14}
                            />

                            <strong>

                              {complaint.districtOfficerName ||
                                complaint.districtOfficer ||
                                "Vikram Patel"}

                            </strong>

                          </div>

                        </td>
                      )}

                      {(activeView ===
                        "STATE" ||
                        activeView ===
                          "CENTRAL") && (

                        <td>

                          <div className="hierarchy-officer">

                            <Globe2
                              size={14}
                            />

                            <strong>

                              {complaint.stateOfficerName ||
                                complaint.stateOfficer ||
                                "Arun Sharma"}

                            </strong>

                          </div>

                        </td>
                      )}

                      <td>

                        <button
                          type="button"
                          className="flow-btn"
                          onClick={e => {

                            e.stopPropagation();

                            setSelectedComplaint(
                              complaint
                            );

                          }}
                        >

                          View Flow

                          <ChevronRight
                            size={13}
                          />

                        </button>

                      </td>

                      <td>

                        <span className="time">

                          {formatDate(
                            complaint.createdAt
                          )}

                        </span>

                      </td>

                    </tr>

                  )
                )}

              </tbody>

            </table>

            {filteredComplaints.length ===
              0 && (

              <div className="empty">
                No complaints found.
              </div>

            )}

          </div>

        </section>

        {/* FOOTER */}

        <footer>

          <span>
            JanSeva AI • Smart Governance
            Platform
          </span>

          <span>
            Secure Government Operations
            <ShieldCheck size={14} />
          </span>

        </footer>

      </main>

      {/* DETAIL MODAL */}

      {selectedComplaint && (

        <div
          className="modal-overlay"
          onClick={() =>
            setSelectedComplaint(
              null
            )
          }
        >

          <div
            className="detail-modal"
            onClick={e =>
              e.stopPropagation()
            }
          >

            <div className="modal-header">

              <div>

                <span>
                  COMPLAINT DETAILS
                </span>

                <h2>

                  {selectedComplaint.complaintId ||
                    selectedComplaint._id ||
                    "Complaint"}

                </h2>

              </div>

              <button
                onClick={() =>
                  setSelectedComplaint(
                    null
                  )
                }
              >

                <X size={20} />

              </button>

            </div>

            <div className="detail-grid">

              <Detail
                label="Department"
                value={
                  selectedComplaint.mainCategory
                }
              />

              <Detail
                label="Problem"
                value={
                  selectedComplaint.subCategory ||
                  selectedComplaint.description
                }
              />

              <Detail
                label="Priority"
                value={
                  selectedComplaint.priority
                }
              />

              <Detail
                label="Status"
                value={
                  selectedComplaint.status
                }
              />

              <Detail
                label="Current Level"
                value={
                  selectedComplaint.currentLevel
                }
              />

              <Detail
                label="Officer Contact"
                value={
                  selectedComplaint.assignedPersonContact
                }
              />

              <Detail
                label="Assigned Officer"
                value={
                  selectedComplaint.assignedPersonName
                }
              />

              <Detail
                label="District Officer"
                value={
                  selectedComplaint.districtOfficerName ||
                  selectedComplaint.districtOfficer ||
                  "Vikram Patel"
                }
              />

              <Detail
                label="State Officer"
                value={
                  selectedComplaint.stateOfficerName ||
                  selectedComplaint.stateOfficer ||
                  "Arun Sharma"
                }
              />

              <Detail
                label="Created At"
                value={
                  formatDate(
                    selectedComplaint.createdAt
                  )
                }
              />

              <Detail
                label="District"
                value={
                  selectedComplaint.district
                }
              />

              <Detail
                label="State"
                value={
                  selectedComplaint.state
                }
              />

              <Detail
                label="Village"
                value={
                  selectedComplaint.village
                }
              />

              <Detail
                label="Ward"
                value={
                  selectedComplaint.ward
                }
              />

            </div>

            <div className="hierarchy-flow">

              <div className="hierarchy-flow-title">

                <ShieldCheck size={16} />

                Complaint Escalation Hierarchy

              </div>

              <div className="hierarchy-flow-row">

                <div className="hierarchy-step active">

                  <small>
                    ASSIGNED
                  </small>

                  <strong>
                    {selectedComplaint.assignedPersonName ||
                      "Not Assigned"}
                  </strong>

                </div>

                <ChevronRight size={16} />

                <div className="hierarchy-step active">

                  <small>
                    DISTRICT
                  </small>

                  <strong>

                    {selectedComplaint.districtOfficerName ||
                      selectedComplaint.districtOfficer ||
                      "Vikram Patel"}

                  </strong>

                </div>

                <ChevronRight size={16} />

                <div className="hierarchy-step active">

                  <small>
                    STATE
                  </small>

                  <strong>

                    {selectedComplaint.stateOfficerName ||
                      selectedComplaint.stateOfficer ||
                      "Arun Sharma"}

                  </strong>

                </div>

                <ChevronRight size={16} />

                <div className="hierarchy-step active">

                  <small>
                    CENTRAL
                  </small>

                  <strong>
                    Central Control
                  </strong>

                </div>

              </div>

            </div>

            <div className="modal-timeline">

              <div className="timeline-item done">

                <CircleDot />

                <div>

                  <strong>
                    Complaint Registered
                  </strong>

                  <span>
                    Citizen complaint
                    received
                  </span>

                </div>

              </div>

              <div className="timeline-item done">

                <CircleDot />

                <div>

                  <strong>
                    Department Assigned
                  </strong>

                  <span>
                    Complaint routed
                    automatically
                  </span>

                </div>

              </div>

              <div className="timeline-item current">

                <CircleDot />

                <div>

                  <strong>
                    Current Processing
                  </strong>

                  <span>
                    Monitoring officer
                    action
                  </span>

                </div>

              </div>

            </div>

          </div>

        </div>

      )}

    </div>
  );
}

/*
 * ============================================================
 * COMPONENTS
 * ============================================================
 */

function KpiCard({
  icon,
  title,
  value,
  subtitle,
  type
}) {

  return (
    <div
      className={`kpi-card ${type}`}
    >

      <div className="kpi-top">

        <div className="kpi-icon">
          {icon}
        </div>

        <span className="kpi-live">
          LIVE
        </span>

      </div>

      <div className="kpi-title">
        {title}
      </div>

      <div className="kpi-value">
        {Number(
          value || 0
        ).toLocaleString()}
      </div>

      <div className="kpi-subtitle">
        {subtitle}
      </div>

    </div>
  );
}


function MetricRow({
  label,
  value,
  total,
  className
}) {

  const percent =
    total > 0
      ? Math.min(
          100,
          Math.round(
            (
              Number(
                value || 0
              ) /
              Number(
                total || 1
              )
            ) * 100
          )
        )
      : 0;

  return (
    <div className="metric-row">

      <div className="metric-top">

        <span>
          {label}
        </span>

        <strong>
          {Number(
            value || 0
          ).toLocaleString()}
        </strong>

      </div>

      <div className="metric-track">

        <span
          className={className}
          style={{
            width:
              `${percent}%`
          }}
        />

      </div>

    </div>
  );
}


function LevelBox({
  icon,
  label,
  value,
  percent
}) {

  return (
    <div className="level-box">

      <div className="level-icon">
        {icon}
      </div>

      <span>
        {label}
      </span>

      <strong>
        {Number(
          value || 0
        ).toLocaleString()}
      </strong>

      <small>
        {percent}% of total
      </small>

    </div>
  );
}


function ActivityItem({
  icon,
  title,
  text,
  time
}) {

  return (
    <div className="activity-item">

      <div className="activity-icon">
        {icon}
      </div>

      <div className="activity-text">

        <strong>
          {title}
        </strong>

        <span>
          {text}
        </span>

      </div>

      <small>
        {time}
      </small>

    </div>
  );
}


function StatusBadge({
  value
}) {

  const status =
    String(
      value ||
      "PENDING"
    ).toUpperCase();

  let cls =
    "status-neutral";

  if (
    status.includes(
      "RESOLVED"
    )
  ) {
    cls =
      "status-resolved";
  }

  else if (
    status.includes(
      "PENDING"
    )
  ) {
    cls =
      "status-pending";
  }

  else if (
    status.includes(
      "ESCALATED"
    )
  ) {
    cls =
      "status-escalated";
  }

  else if (
    status.includes(
      "PROGRESS"
    )
  ) {
    cls =
      "status-progress";
  }

  return (
    <span
      className={
        `status-badge ${cls}`
      }
    >

      <span />

      {status}

    </span>
  );
}


function PriorityBadge({
  value
}) {

  const priority =
    String(
      value ||
      "NORMAL"
    ).toUpperCase();

  return (
    <span
      className={
        `priority-badge ${
          priority ===
          "CRITICAL"
            ? "critical"
            : priority ===
              "HIGH"
            ? "high"
            : "normal"
        }`
      }
    >

      {priority}

    </span>
  );
}


function Detail({
  label,
  value
}) {

  return (
    <div className="detail">

      <span>
        {label}
      </span>

      <strong>
        {formatObjectValue(
          value
        )}
      </strong>

    </div>
  );
}


/*
 * ============================================================
 * HELPERS
 * ============================================================
 */

function normalizeLevel(
  level
) {

  return String(
    level || ""
  )
    .trim()
    .toUpperCase();

}


function safePercent(
  value,
  total
) {

  if (!total) {
    return 0;
  }

  return Math.min(
    100,
    Math.round(
      (
        Number(
          value || 0
        ) /
        Number(
          total || 1
        )
      ) * 100
    )
  );

}


/*
 * ============================================================
 * CALCULATE STATS
 * ============================================================
 */

function calculateStats(
  data,
  fallback = {}
) {

  const list =
    Array.isArray(data)
      ? data
      : [];

  if (
    list.length === 0 &&
    fallback?.totalComplaints
  ) {
    return fallback;
  }

  const statusOf =
    complaint =>
      String(
        complaint?.status ||
        ""
      )
        .trim()
        .toUpperCase();

  const levelOf =
    complaint =>
      normalizeLevel(
        complaint?.currentLevel
      );

  return {

    totalComplaints:
      list.length,

    pending:
      list.filter(
        c =>
          statusOf(c).includes(
            "PENDING"
          )
      ).length,

    resolved:
      list.filter(
        c =>
          statusOf(c).includes(
            "RESOLVED"
          )
      ).length,

    escalatedCount:
      list.filter(
        c =>
          statusOf(c).includes(
            "ESCALATED"
          )
      ).length,

    villageLevel:
      list.filter(
        c =>
          levelOf(c) ===
          "VILLAGE"
      ).length,

    districtLevel:
      list.filter(
        c =>
          levelOf(c) ===
          "DISTRICT"
      ).length,

    stateLevel:
      list.filter(
        c =>
          [
            "STATE",
            "CENTRAL"
          ].includes(
            levelOf(c)
          )
      ).length

  };

}


/*
 * ============================================================
 * LIVE ACTIVITY
 * ============================================================
 */

function getActivityItems(
  data
) {

  const list =
    Array.isArray(data)
      ? data
      : [];

  const sorted =
    [...list].sort(
      (a, b) => {

        const da =
          new Date(
            a?.createdAt ||
              0
          ).getTime();

        const db =
          new Date(
            b?.createdAt ||
              0
          ).getTime();

        if (
          Number.isNaN(da) ||
          Number.isNaN(db)
        ) {
          return 0;
        }

        return db - da;

      }
    );

  const latest =
    sorted.slice(
      0,
      4
    );

  if (
    latest.length === 0
  ) {

    return [
      {
        icon: "+",
        title:
          "No recent activity",
        text:
          "No complaint records available",
        time: "—"
      }
    ];

  }

  return latest.map(
    complaint => {

      const status =
        String(
          complaint?.status ||
            ""
        ).toUpperCase();

      let icon = "+";

      let title =
        "New complaint registered";

      if (
        status.includes(
          "RESOLVED"
        )
      ) {

        icon = "✓";

        title =
          "Complaint resolved";

      }

      else if (
        status.includes(
          "ESCALATED"
        )
      ) {

        icon = "↗";

        title =
          "Complaint escalated";

      }

      else if (
        status.includes(
          "PENDING"
        )
      ) {

        icon = "!";

        title =
          "Pending complaint";

      }

      else if (
        status.includes(
          "PROGRESS"
        )
      ) {

        icon = "•";

        title =
          "Complaint in progress";

      }

      return {

        icon,

        title,

        text:
          complaint?.complaintId ||
          complaint?._id ||
          complaint?.mainCategory ||
          "Complaint",

        time:
          relativeTime(
            complaint?.createdAt
          )

      };

    }
  );

}


function relativeTime(
  value
) {

  if (!value) {
    return "—";
  }

  const date =
    new Date(value);

  if (
    Number.isNaN(
      date.getTime()
    )
  ) {
    return String(value);
  }

  const diff =
    Math.max(
      0,
      Date.now() -
        date.getTime()
    );

  const minutes =
    Math.floor(
      diff / 60000
    );

  if (
    minutes < 1
  ) {
    return "just now";
  }

  if (
    minutes < 60
  ) {
    return `${minutes} min ago`;
  }

  const hours =
    Math.floor(
      minutes / 60
    );

  if (
    hours < 24
  ) {
    return `${hours} hr ago`;
  }

  const days =
    Math.floor(
      hours / 24
    );

  return `${days} day${
    days === 1
      ? ""
      : "s"
  } ago`;

}


function getDepartmentIcon(
  name
) {

  const icons = {

    Water: "💧",

    Electricity: "⚡",

    Roads: "🛣️",

    Health: "🏥",

    Police: "🛡️",

    Garbage: "♻️",

    Drainage: "🌊",

    Education: "🎓",

    Agriculture: "🌾",

    Transport: "🚌",

    Housing: "🏠",

    Internet: "🌐"

  };

  return (
    icons[name] ||
    "📋"
  );

}


function formatObjectValue(
  value
) {

  if (
    value === null ||
    value === undefined ||
    value === ""
  ) {

    return "—";

  }

  if (
    typeof value ===
    "object"
  ) {

    if (
      Array.isArray(value)
    ) {

      return value.join(
        ", "
      );

    }

    return Object.entries(
      value
    )
      .map(
        ([key, val]) =>
          `${key}: ${formatObjectValue(
            val
          )}`
      )
      .join(" • ");

  }

  return String(value);

}


function formatDate(
  date
) {

  if (!date) {
    return "—";
  }

  if (
    typeof date ===
    "string"
  ) {

    const parsed =
      new Date(date);

    if (
      Number.isNaN(
        parsed.getTime()
      )
    ) {

      return date;

    }

    return parsed.toLocaleString(
      "en-IN",
      {
        day: "2-digit",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
      }
    );

  }

  try {

    return new Date(
      date
    ).toLocaleString(
      "en-IN",
      {
        day: "2-digit",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit"
      }
    );

  } catch {

    return "—";

  }

}