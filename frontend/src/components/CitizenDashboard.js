import React, {
    useEffect,
    useState
} from 'react';

import axios from 'axios';

import {
    Plus,
    Search,
    X,
    Camera,
    Video,
    FileText,
    Bot,
    LayoutGrid,
    ShieldCheck,
    History,
    MapPin,
    Navigation,
    Map,
    WifiOff,
    AlertTriangle
} from 'lucide-react';

import './CitizenDashboard.css';


const categoryIcons = {

    Water: "💧",
    Health: "🏥",
    Agriculture: "🌾",
    Education: "📚",
    Electricity: "⚡",
    Roads: "🛣️",
    Police: "👮",
    Internet: "🌐",
    Drainage: "🚰",
    Garbage: "🗑️",
    Transport: "🚌",
    Banking: "🏦",
    Housing: "🏠",
    Sanitation: "🧹",
    Tourism: "🗺️",
    Fire: "🔥",
    Women: "👩",
    Child: "🧒",
    Pension: "💰",
    Employment: "💼",
    Other: "✨"

};


const CitizenDashboard = () => {


    /* =====================================================
       STATE
    ===================================================== */

    const [categories, setCategories] =
        useState([]);

    const [query, setQuery] =
        useState('');

    const [chatHistory, setChatHistory] =
        useState([]);

    const [trackingMobile, setTrackingMobile] =
        useState('');

    const [trackingData, setTrackingData] =
        useState([]);

    const [showMenu, setShowMenu] =
        useState(false);

    const [showComplaint, setShowComplaint] =
        useState(false);

    const [subProblems, setSubProblems] =
        useState([]);

    const [locationType, setLocationType] =
        useState('current');

    const [savedLocation, setSavedLocation] =
        useState(null);

    const [networkOnline, setNetworkOnline] =
        useState(navigator.onLine);

    const [aiStatus, setAiStatus] =
        useState('ONLINE');


    const [activeSection, setActiveSection] =
        useState('departments');


    const [complaintData, setComplaintData] =
        useState({

            mainCategory: '',
            subCategory: '',
            description: '',
            userMobile: '',

            latitude: 0,
            longitude: 0,

            areaType: '',
            village: '',
            block: '',
            district: '',
            state: '',
            city: '',
            ward: ''

        });


    /* =====================================================
       LOAD CATEGORIES
    ===================================================== */

    useEffect(() => {

        axios.get('/api/categories')

            .then((res) => {

                setCategories(res.data);

            })

            .catch(() => {

                setCategories([

                    {
                        mainCategory: "Water",
                        subCategories: [
                            "Pipeline Leakage",
                            "No Supply",
                            "Dirty Water"
                        ]
                    },

                    {
                        mainCategory: "Electricity",
                        subCategories: [
                            "Power Cut",
                            "Meter Issue",
                            "Wire Sparking"
                        ]
                    },

                    {
                        mainCategory: "Roads",
                        subCategories: [
                            "Potholes",
                            "Street Damage"
                        ]
                    },

                    {
                        mainCategory: "Health",
                        subCategories: [
                            "Hospital Problem",
                            "Medicine Shortage"
                        ]
                    },

                    {
                        mainCategory: "Police",
                        subCategories: [
                            "Theft",
                            "Violence",
                            "Harassment"
                        ]
                    },

                    {
                        mainCategory: "Garbage",
                        subCategories: [
                            "Garbage Collection",
                            "Dirty Area"
                        ]
                    }

                ]);

            });

    }, []);


    /* =====================================================
       OFFLINE STORAGE
    ===================================================== */

    const saveOfflineComplaint = (data) => {

        let offlineComplaints =
            JSON.parse(
                localStorage.getItem(
                    "offlineComplaints"
                ) || "[]"
            );

        offlineComplaints.push({

            ...data,

            savedAt:
                new Date().toISOString()

        });

        localStorage.setItem(

            "offlineComplaints",

            JSON.stringify(
                offlineComplaints
            )

        );

    };


    /* =====================================================
       RESEND OFFLINE COMPLAINTS
    ===================================================== */

    useEffect(() => {

        const resendOffline = async () => {

            const saved =
                JSON.parse(

                    localStorage.getItem(
                        "offlineComplaints"
                    ) || "[]"

                );

            if (saved.length === 0) {
                return;
            }

            for (const c of saved) {

                try {

                    await axios.post(
                        '/api/complaint',
                        c
                    );

                }

                catch (err) {

                    console.log(err);

                }

            }

            localStorage.removeItem(
                'offlineComplaints'
            );

        };


        const onlineHandler = () => {

            setNetworkOnline(true);

            resendOffline();

        };


        const offlineHandler = () => {

            setNetworkOnline(false);

        };


        window.addEventListener(
            'online',
            onlineHandler
        );

        window.addEventListener(
            'offline',
            offlineHandler
        );


        return () => {

            window.removeEventListener(
                'online',
                onlineHandler
            );

            window.removeEventListener(
                'offline',
                offlineHandler
            );

        };

    }, []);


    /* =====================================================
       LOCATION
    ===================================================== */

    const getCurrentLocation = () => {

        if (!navigator.geolocation) {

            alert(
                'Geolocation Not Supported'
            );

            return;

        }


        navigator.geolocation.getCurrentPosition(

            (position) => {

                const lat =
                    position.coords.latitude;

                const lng =
                    position.coords.longitude;


                setComplaintData(
                    (prev) => ({

                        ...prev,

                        latitude: lat,

                        longitude: lng

                    })
                );


                const newLocation = {

                    latitude: lat,

                    longitude: lng

                };


                setSavedLocation(
                    newLocation
                );


                localStorage.setItem(

                    'savedLocation',

                    JSON.stringify(
                        newLocation
                    )

                );

            },


            () => {

                alert(
                    'Location Permission Denied'
                );

            }

        );

    };


    useEffect(() => {

        getCurrentLocation();


        const oldLocation =
            localStorage.getItem(
                'savedLocation'
            );


        if (oldLocation) {

            setSavedLocation(

                JSON.parse(
                    oldLocation
                )

            );

        }

    }, []);


    /* =====================================================
       SAVED MAP
    ===================================================== */

    const openSavedLocationMap = () => {

        if (!savedLocation) {

            alert(
                'No Saved Location'
            );

            return;

        }


        window.open(

            `https://www.google.com/maps?q=${savedLocation.latitude},${savedLocation.longitude}`,

            '_blank'

        );

    };


    /* =====================================================
       AI SEARCH
    ===================================================== */

    const handleAISearch = async () => {

        if (!query.trim()) {
            return;
        }


        const userQuery = query;


        setChatHistory(
            (prev) => [

                ...prev,

                {
                    type: 'user',
                    text: userQuery
                }

            ]
        );


        setQuery('');


        try {

            const response =
                await axios.post(

                    'http://127.0.0.1:8000/classify',

                    {

                        query: userQuery,

                        latitude:
                            complaintData.latitude,

                        longitude:
                            complaintData.longitude,

                        city:
                            complaintData.city,

                        state:
                            complaintData.state

                    }

                );


            const aiData =
                response.data;


            console.log(
                "AI RESPONSE:",
                aiData
            );


            /* =================================================
               SAFE AI RESPONSE CONVERSION
            ================================================= */

            let botReply =
                "AI Response received.";


            /*
             * CASE 1
             * Backend directly returns string
             */

            if (
                typeof aiData ===
                "string"
            ) {

                botReply =
                    aiData;

            }


            /*
             * CASE 2
             * answer exists
             */

            else if (
                aiData?.answer
            ) {

                if (
                    typeof aiData.answer ===
                    "string"
                ) {

                    botReply =
                        aiData.answer;

                }

                else {

                    botReply =

                        aiData.answer?.message ||

                        aiData.answer?.summary ||

                        "AI Response received.";

                }

            }


            /*
             * CASE 3
             * message exists
             */

            else if (
                aiData?.message
            ) {

                if (
                    typeof aiData.message ===
                    "string"
                ) {

                    botReply =
                        aiData.message;

                }

                else {

                    botReply =

                        aiData.message?.message ||

                        aiData.message?.summary ||

                        "AI Response received.";

                }

            }


            /*
             * CASE 4
             * summary exists
             */

            else if (
                aiData?.summary
            ) {

                botReply =
                    aiData.summary;

            }


            /* =================================================
               EMERGENCY
            ================================================= */

            if (
                aiData?.emergency ===
                true
            ) {

                botReply =

                    "🚨 EMERGENCY DETECTED\n\n" +

                    botReply;

            }


            /* =================================================
               FAKE COMPLAINT
            ================================================= */

            if (
                aiData?.fake_detected ===
                true
            ) {

                botReply =

                    botReply +

                    "\n\n⚠️ Fake Complaint Suspected";

            }


            /* =================================================
               PRIORITY
            ================================================= */

            if (
                aiData?.priority
            ) {

                botReply =

                    botReply +

                    "\nPriority: " +

                    String(
                        aiData.priority
                    );

            }


            /* =================================================
               DEPARTMENT
            ================================================= */

            if (
                aiData?.department
            ) {

                botReply =

                    botReply +

                    "\nDepartment: " +

                    String(
                        aiData.department
                    );

            }


            /* =================================================
               CONFIDENCE
            ================================================= */

            if (
                aiData?.confidence !==
                undefined
            ) {

                botReply =

                    botReply +

                    "\nConfidence: " +

                    String(
                        aiData.confidence
                    );

            }


            /* =================================================
               SENTIMENT
            ================================================= */

            if (
                aiData?.sentiment
            ) {

                botReply =

                    botReply +

                    "\nSentiment: " +

                    String(
                        aiData.sentiment
                    );

            }


            /* =================================================
               FINAL SAFETY
            ================================================= */

            if (
                typeof botReply !==
                "string"
            ) {

                botReply =
                    JSON.stringify(
                        botReply,
                        null,
                        2
                    );

            }


            setChatHistory(
                (prev) => [

                    ...prev,

                    {
                        type: 'bot',
                        text: botReply
                    }

                ]
            );


            setAiStatus(
                'ONLINE'
            );

        }


        catch (error) {

            console.error(
                "AI ERROR:",
                error
            );


            setAiStatus(
                'OFFLINE'
            );


            setChatHistory(
                (prev) => [

                    ...prev,

                    {
                        type: 'bot',

                        text:
                            'AI Server Offline'
                    }

                ]
            );

        }

    };


    /* =====================================================
       OPEN COMPLAINT
    ===================================================== */

    const openComplaintModal = (cat) => {

        setSubProblems(
            cat.subCategories || []
        );


        setComplaintData(
            (prev) => ({

                ...prev,

                mainCategory:
                    cat.mainCategory,

                subCategory: ''

            })
        );


        setShowComplaint(
            true
        );

    };


    /* =====================================================
       SUBMIT COMPLAINT
    ===================================================== */

    const submitComplaint = async () => {

        if (!navigator.onLine) {

            saveOfflineComplaint(
                complaintData
            );


            alert(
                'No Network. Complaint Saved Offline.'
            );

            return;

        }


        try {

            await axios.post(

                '/api/complaint',

                complaintData

            );


            alert(
                'Complaint Submitted'
            );


            setShowComplaint(
                false
            );


            setComplaintData({

                mainCategory: '',
                subCategory: '',
                description: '',
                userMobile: '',

                latitude: 0,
                longitude: 0,

                areaType: '',
                village: '',
                block: '',
                district: '',
                state: '',
                city: '',
                ward: ''

            });

        }

        catch {

            alert(
                'Submission Failed'
            );

        }

    };


    /* =====================================================
       TRACK COMPLAINT
    ===================================================== */

    const trackComplaint = async () => {

        if (!trackingMobile.trim()) {

            alert(
                'Enter Mobile Number'
            );

            return;

        }


        try {

            const response =
                await axios.get(

                    `/api/track/${trackingMobile}`

                );


            setTrackingData(

                Array.isArray(
                    response.data
                )

                    ?

                    response.data

                    :

                    [response.data]

            );

        }

        catch {

            setTrackingData([]);

            alert(
                'Tracking Failed'
            );

        }

    };


    /* =====================================================
       SIDEBAR BUTTON
    ===================================================== */

    const changeSection = (section) => {

        setActiveSection(
            section
        );

    };


    /* =====================================================
       RENDER
    ===================================================== */

    return (

        <div className="dashboard">


            {/* =================================================
                TOP BAR
            ================================================= */}

            <header className="topbar">

                <div className="logo">

                    JanSeva AI 🇮🇳

                </div>


                <div className="top-status">

                    {

                        networkOnline

                            ?

                            (

                                <div className="online-status">

                                    <span className="status-dot"></span>

                                    NETWORK ONLINE

                                </div>

                            )

                            :

                            (

                                <div className="offline-status">

                                    <WifiOff size={13}/>

                                    OFFLINE MODE

                                </div>

                            )

                    }


                    <div className="ai-top-status">

                        AI {aiStatus}

                    </div>

                </div>

            </header>


            {/* =================================================
                SIDEBAR
            ================================================= */}

            <aside className="sidebar">


                <div className="sidebar-brand">

                    <div className="brand-icon">

                        🇮🇳

                    </div>


                    <div>

                        <div className="brand-title">

                            JANSEVA AI

                        </div>

                        <div className="brand-subtitle">

                            CITIZEN PORTAL

                        </div>

                    </div>

                </div>


                <div className="sidebar-divider"></div>


                <div className="sidebar-label">

                    SERVICES

                </div>


                {/* DEPARTMENT */}

                <button

                    className={

                        activeSection ===
                        'departments'

                            ?

                            'sidebar-btn active'

                            :

                            'sidebar-btn'

                    }

                    onClick={() =>
                        changeSection(
                            'departments'
                        )
                    }

                >

                    <span className="sidebar-icon">

                        <LayoutGrid size={18}/>

                    </span>


                    <span>

                        Departments

                    </span>


                    {

                        activeSection ===
                        'departments'

                            &&

                            <span className="active-arrow">

                                ›

                            </span>

                    }

                </button>


                {/* AI */}

                <button

                    className={

                        activeSection ===
                        'ai'

                            ?

                            'sidebar-btn active'

                            :

                            'sidebar-btn'

                    }

                    onClick={() =>
                        changeSection(
                            'ai'
                        )
                    }

                >

                    <span className="sidebar-icon">

                        <Bot size={18}/>

                    </span>


                    <span>

                        AI Assistant

                    </span>


                    {

                        activeSection ===
                        'ai'

                            &&

                            <span className="active-arrow">

                                ›

                            </span>

                    }

                </button>


                {/* TRACK */}

                <button

                    className={

                        activeSection ===
                        'track'

                            ?

                            'sidebar-btn active'

                            :

                            'sidebar-btn'

                    }

                    onClick={() =>
                        changeSection(
                            'track'
                        )
                    }

                >

                    <span className="sidebar-icon">

                        <ShieldCheck size={18}/>

                    </span>


                    <span>

                        Track Complaint

                    </span>


                    {

                        activeSection ===
                        'track'

                            &&

                            <span className="active-arrow">

                                ›

                            </span>

                    }

                </button>


                {/* EMERGENCY */}

                <button

                    className={

                        activeSection ===
                        'emergency'

                            ?

                            'sidebar-btn active emergency-side'

                            :

                            'sidebar-btn emergency-side'

                    }

                    onClick={() =>
                        changeSection(
                            'emergency'
                        )
                    }

                >

                    <span className="sidebar-icon">

                        <AlertTriangle size={18}/>

                    </span>


                    <span>

                        Emergency AI

                    </span>


                    {

                        activeSection ===
                        'emergency'

                            &&

                            <span className="active-arrow">

                                ›

                            </span>

                    }

                </button>


                <div className="sidebar-bottom">


                    <div className="security-mini">

                        <ShieldCheck size={15}/>

                        <span>

                            Secure Governance

                        </span>

                    </div>


                    <div className="version-mini">

                        JanSeva AI v1.0

                    </div>

                </div>

            </aside>


            {/* =================================================
                MAIN CONTENT
            ================================================= */}

            <main className="content-area">


                {/* HERO */}

                <section className="hero-section">

                    <div className="hero-left">

                        <div className="ai-badge">

                            <Bot size={12}/>

                            AI GOVERNANCE PLATFORM

                        </div>


                        <h1>

                            Smart Citizen Portal

                        </h1>


                        <p>

                            AI Powered Governance
                            & Complaint Management System

                        </p>

                    </div>


                    <div

                        className="saved-map-icon"

                        onClick={
                            openSavedLocationMap
                        }

                    >

                        <Map size={18}/>

                    </div>

                </section>


                {/* =================================================
                    DEPARTMENTS
                ================================================= */}

                {

                    activeSection ===
                    'departments'

                    &&

                    <section className="modern-card page-card">


                        <div className="section-header-large">

                            <div>

                                <div className="section-title">

                                    <LayoutGrid size={17}/>

                                    Departments

                                </div>


                                <p className="section-description">

                                    Select a department to submit
                                    a citizen complaint.

                                </p>

                            </div>


                            <div className="section-count">

                                {categories.length}

                                <span>

                                    Departments

                                </span>

                            </div>

                        </div>


                        <div className="department-grid">


                            {

                                categories.map(
                                    (cat, index) => (

                                        <div

                                            key={index}

                                            className="department-item"

                                            onClick={() =>
                                                openComplaintModal(
                                                    cat
                                                )
                                            }

                                        >

                                            <div className="department-icon">

                                                {

                                                    categoryIcons[
                                                        cat.mainCategory
                                                    ]

                                                    ||

                                                    "✨"

                                                }

                                            </div>


                                            <span>

                                                {
                                                    cat.mainCategory
                                                }

                                            </span>


                                            <small>

                                                Report Issue

                                            </small>

                                        </div>

                                    )
                                )

                            }

                        </div>

                    </section>

                }


                {/* =================================================
                    AI ASSISTANT
                ================================================= */}

                {

                    activeSection ===
                    'ai'

                    &&

                    <section className="modern-card page-card ai-page-card">


                        <div className="section-header-large">

                            <div>

                                <div className="section-title">

                                    <Bot size={17}/>

                                    AI Assistant

                                </div>


                                <p className="section-description">

                                    Ask JanSeva AI about complaints,
                                    departments, emergencies and governance.

                                </p>

                            </div>


                            <div className="ai-live-pill">

                                <span></span>

                                AI {aiStatus}

                            </div>

                        </div>


                        <div className="chat-area big-chat-area">


                            {

                                chatHistory.length === 0

                                    ?

                                    <div className="ai-welcome">

                                        <div className="ai-welcome-icon">

                                            <Bot size={28}/>

                                        </div>


                                        <h3>

                                            How can I help you?

                                        </h3>


                                        <p>

                                            Describe your issue or ask
                                            anything about JanSeva services.

                                        </p>

                                    </div>

                                    :

                                    chatHistory.map(
                                        (msg, index) => (

                                            <div

                                                key={index}

                                                className={

                                                    msg.type ===
                                                    'user'

                                                        ?

                                                        'user-msg'

                                                        :

                                                        'bot-msg'

                                                }

                                            >

                                                {

                                                    typeof msg.text ===
                                                    'string'

                                                        ?

                                                        msg.text

                                                        :

                                                        JSON.stringify(
                                                            msg.text
                                                        )

                                                }

                                            </div>

                                        )
                                    )

                            }

                        </div>


                        <div className="chat-bottom">


                            <div className="chat-input">


                                <button

                                    className="icon-btn"

                                    onClick={() =>
                                        setShowMenu(
                                            !showMenu
                                        )
                                    }

                                >

                                    <Plus size={16}/>

                                </button>


                                <input

                                    type="text"

                                    placeholder="Ask JanSeva AI..."

                                    value={query}

                                    onChange={(e) =>
                                        setQuery(
                                            e.target.value
                                        )
                                    }

                                    onKeyDown={(e) => {

                                        if (
                                            e.key ===
                                            'Enter'
                                        ) {

                                            handleAISearch();

                                        }

                                    }}

                                />


                                <button

                                    className="send-btn"

                                    onClick={
                                        handleAISearch
                                    }

                                >

                                    <Search size={15}/>

                                </button>


                            </div>


                            {

                                showMenu

                                &&

                                <div className="upload-menu">


                                    <div className="upload-item">

                                        <Camera size={15}/>

                                        Image

                                    </div>


                                    <div className="upload-item">

                                        <Video size={15}/>

                                        Video

                                    </div>


                                    <div className="upload-item">

                                        <FileText size={15}/>

                                        File

                                    </div>

                                </div>

                            }

                        </div>

                    </section>

                }


                {/* =================================================
                    TRACK
                ================================================= */}

                {

                    activeSection ===
                    'track'

                    &&

                    <section className="modern-card page-card">


                        <div className="section-header-large">

                            <div>

                                <div className="section-title">

                                    <ShieldCheck size={17}/>

                                    Track Complaint

                                </div>


                                <p className="section-description">

                                    Enter your registered mobile
                                    number to track complaint status.

                                </p>

                            </div>

                        </div>


                        <div className="track-box">


                            <div className="track-search-box">

                                <input

                                    type="text"

                                    placeholder="Enter Mobile Number"

                                    value={
                                        trackingMobile
                                    }

                                    onChange={(e) =>
                                        setTrackingMobile(
                                            e.target.value
                                        )
                                    }

                                />


                                <button

                                    className="send-btn track-button"

                                    onClick={
                                        trackComplaint
                                    }

                                >

                                    Track

                                </button>

                            </div>


                            {

                                trackingData.length ===
                                0

                                    ?

                                    <div className="history-box">

                                        <div>

                                            <ShieldCheck
                                                size={30}
                                            />

                                            <p>

                                                No Complaint Found

                                            </p>

                                            <small>

                                                Enter your mobile
                                                number above to track.

                                            </small>

                                        </div>

                                    </div>

                                    :

                                    trackingData.map(
                                        (item, index) => (

                                            <div

                                                key={index}

                                                className="track-card"

                                            >

                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Complaint ID

                                                    </span>

                                                    <span className="track-value">

                                                        {
                                                            item.complaintId
                                                            ||
                                                            "N/A"
                                                        }

                                                    </span>

                                                </div>


                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Status

                                                    </span>

                                                    <span className="track-value status-badge">

                                                        {
                                                            item.status
                                                            ||
                                                            "Pending"
                                                        }

                                                    </span>

                                                </div>


                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Department

                                                    </span>

                                                    <span className="track-value">

                                                        {
                                                            item.mainCategory
                                                            ||
                                                            "N/A"
                                                        }

                                                    </span>

                                                </div>


                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Current Level

                                                    </span>

                                                    <span className="track-value">

                                                        {
                                                            item.currentLevel
                                                            ||
                                                            "Pending"
                                                        }

                                                    </span>

                                                </div>


                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Assigned Officer

                                                    </span>

                                                    <span className="track-value">

                                                        {
                                                            item.assignedPersonName
                                                            ||
                                                            "Not Assigned"
                                                        }

                                                    </span>

                                                </div>


                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Officer Contact

                                                    </span>

                                                    <span className="track-value">

                                                        {
                                                            item.assignedPersonContact
                                                            ||
                                                            "N/A"
                                                        }

                                                    </span>

                                                </div>


                                                <div className="track-row">

                                                    <span className="track-label">

                                                        Date & Time

                                                    </span>

                                                    <span className="track-value">

                                                        {

                                                            item.createdAt

                                                                ?

                                                                new Date(
                                                                    item.createdAt
                                                                ).toLocaleString()

                                                                :

                                                                "N/A"

                                                        }

                                                    </span>

                                                </div>

                                            </div>

                                        )
                                    )

                            }

                        </div>

                    </section>

                }


                {/* =================================================
                    EMERGENCY
                ================================================= */}

                {

                    activeSection ===
                    'emergency'

                    &&

                    <section className="modern-card page-card emergency-page-card">


                        <div className="section-header-large">

                            <div>

                                <div className="section-title emergency-title">

                                    <AlertTriangle size={18}/>

                                    Emergency AI

                                </div>


                                <p className="section-description">

                                    AI-powered emergency and
                                    complaint safety monitoring.

                                </p>

                            </div>


                            <div className="emergency-status">

                                ACTIVE

                            </div>

                        </div>


                        <div className="emergency-grid">


                            <div className="emergency-main">


                                <div className="emergency-icon">

                                    <AlertTriangle size={32}/>

                                </div>


                                <h2>

                                    Emergency AI Active

                                </h2>


                                <p>

                                    JanSeva AI can identify
                                    emergency-related complaints
                                    and flag them for priority handling.

                                </p>


                                <button

                                    className="emergency-action"

                                    onClick={() =>
                                        setActiveSection(
                                            'ai'
                                        )
                                    }

                                >

                                    <Bot size={15}/>

                                    Ask AI Assistant

                                </button>

                            </div>


                            <div className="emergency-features">


                                <div className="feature-box">

                                    <AlertTriangle size={18}/>

                                    <div>

                                        <strong>

                                            Emergency Detection

                                        </strong>

                                        <span>

                                            AI identifies urgent cases.

                                        </span>

                                    </div>

                                </div>


                                <div className="feature-box">

                                    <Bot size={18}/>

                                    <div>

                                        <strong>

                                            AI Moderation

                                        </strong>

                                        <span>

                                            Suspicious complaints can
                                            be flagged.

                                        </span>

                                    </div>

                                </div>


                                <div className="feature-box">

                                    <History size={18}/>

                                    <div>

                                        <strong>

                                            Complaint Monitoring

                                        </strong>

                                        <span>

                                            Status and escalation
                                            can be tracked.

                                        </span>

                                    </div>

                                </div>


                                <div className="feature-box">

                                    <WifiOff size={18}/>

                                    <div>

                                        <strong>

                                            Offline Queue

                                        </strong>

                                        <span>

                                            Complaints can be saved
                                            when network is unavailable.

                                        </span>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </section>

                }

            </main>


            {/* =================================================
                COMPLAINT MODAL
            ================================================= */}

            {

                showComplaint

                &&

                <div className="modal-overlay">


                    <div className="complaint-modal">


                        <div className="modal-top">

                            <h2>

                                Submit Complaint

                            </h2>


                            <button

                                className="close-btn"

                                onClick={() =>
                                    setShowComplaint(
                                        false
                                    )
                                }

                            >

                                <X size={16}/>

                            </button>

                        </div>


                        <div className="location-box">


                            <button

                                className={

                                    locationType ===
                                    'current'

                                        ?

                                        'location-btn active-location'

                                        :

                                        'location-btn'

                                }

                                onClick={() => {

                                    setLocationType(
                                        'current'
                                    );

                                    getCurrentLocation();

                                }}

                            >

                                <Navigation size={14}/>

                                Current

                            </button>


                            <button

                                className={

                                    locationType ===
                                    'manual'

                                        ?

                                        'location-btn active-location'

                                        :

                                        'location-btn'

                                }

                                onClick={() =>
                                    setLocationType(
                                        'manual'
                                    )
                                }

                            >

                                <MapPin size={14}/>

                                Manual

                            </button>

                        </div>


                        {

                            locationType ===
                            'manual'

                            &&

                            <>

                                <input

                                    type="text"

                                    placeholder="Latitude"

                                    value={
                                        complaintData.latitude
                                    }

                                    onChange={(e) =>
                                        setComplaintData({

                                            ...complaintData,

                                            latitude:
                                                e.target.value

                                        })
                                    }

                                />


                                <input

                                    type="text"

                                    placeholder="Longitude"

                                    value={
                                        complaintData.longitude
                                    }

                                    onChange={(e) =>
                                        setComplaintData({

                                            ...complaintData,

                                            longitude:
                                                e.target.value

                                        })
                                    }

                                />

                            </>

                        }


                        <select

                            value={
                                complaintData.areaType
                            }

                            onChange={(e) =>
                                setComplaintData({

                                    ...complaintData,

                                    areaType:
                                        e.target.value

                                })
                            }

                        >

                            <option value="">

                                Select Area Type

                            </option>

                            <option value="RURAL">

                                Rural

                            </option>

                            <option value="URBAN">

                                Urban

                            </option>

                        </select>


                        {

                            complaintData.areaType ===
                            'RURAL'

                            &&

                            <>

                                <input

                                    type="text"

                                    placeholder="Village"

                                    value={
                                        complaintData.village
                                    }

                                    onChange={(e) =>
                                        setComplaintData({

                                            ...complaintData,

                                            village:
                                                e.target.value

                                        })
                                    }

                                />


                                <input

                                    type="text"

                                    placeholder="Block"

                                    value={
                                        complaintData.block
                                    }

                                    onChange={(e) =>
                                        setComplaintData({

                                            ...complaintData,

                                            block:
                                                e.target.value

                                        })
                                    }

                                />

                            </>

                        }


                        {

                            complaintData.areaType ===
                            'URBAN'

                            &&

                            <>

                                <input

                                    type="text"

                                    placeholder="City"

                                    value={
                                        complaintData.city
                                    }

                                    onChange={(e) =>
                                        setComplaintData({

                                            ...complaintData,

                                            city:
                                                e.target.value

                                        })
                                    }

                                />


                                <input

                                    type="text"

                                    placeholder="Ward"

                                    value={
                                        complaintData.ward
                                    }

                                    onChange={(e) =>
                                        setComplaintData({

                                            ...complaintData,

                                            ward:
                                                e.target.value

                                        })
                                    }

                                />

                            </>

                        }


                        <input

                            type="text"

                            placeholder="District"

                            value={
                                complaintData.district
                            }

                            onChange={(e) =>
                                setComplaintData({

                                    ...complaintData,

                                    district:
                                        e.target.value

                                })
                            }

                        />


                        <input

                            type="text"

                            placeholder="State"

                            value={
                                complaintData.state
                            }

                            onChange={(e) =>
                                setComplaintData({

                                    ...complaintData,

                                    state:
                                        e.target.value

                                })
                            }

                        />


                        <input

                            type="text"

                            value={
                                complaintData.mainCategory
                            }

                            readOnly

                        />


                        <select

                            value={
                                complaintData.subCategory
                            }

                            onChange={(e) =>
                                setComplaintData({

                                    ...complaintData,

                                    subCategory:
                                        e.target.value

                                })
                            }

                        >

                            <option value="">

                                Select Problem

                            </option>


                            {

                                subProblems.map(
                                    (s, i) => (

                                        <option

                                            key={i}

                                            value={s}

                                        >

                                            {s}

                                        </option>

                                    )
                                )

                            }


                            <option value="Other">

                                Other

                            </option>

                        </select>


                        <textarea

                            rows="4"

                            placeholder="Describe issue"

                            value={
                                complaintData.description
                            }

                            onChange={(e) =>
                                setComplaintData({

                                    ...complaintData,

                                    description:
                                        e.target.value

                                })
                            }

                        />


                        <input

                            type="text"

                            placeholder="Mobile Number"

                            value={
                                complaintData.userMobile
                            }

                            onChange={(e) =>
                                setComplaintData({

                                    ...complaintData,

                                    userMobile:
                                        e.target.value

                                })
                            }

                        />


                        <button

                            className="submit-btn"

                            onClick={
                                submitComplaint
                            }

                        >

                            Submit Complaint

                        </button>


                    </div>

                </div>

            }

        </div>

    );

};


export default CitizenDashboard;