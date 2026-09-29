from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

import google.generativeai as genai
import uvicorn
import json
import re
import os

from datetime import datetime
from dotenv import load_dotenv


# =========================================================
# ENVIRONMENT
# =========================================================

load_dotenv()

GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")


# =========================================================
# GEMINI CONFIG
# =========================================================

if GEMINI_API_KEY:
    genai.configure(api_key=GEMINI_API_KEY)

    model = genai.GenerativeModel(
        "gemini-2.5-flash"
    )
else:
    model = None


# =========================================================
# FASTAPI
# =========================================================

app = FastAPI(
    title="JanSeva AI",
    description="Advanced Governance AI",
    version="7.0"
)


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)


# =========================================================
# MEMORY
# =========================================================

chat_history = []

recent_complaints = []

user_behavior = {}


# =========================================================
# REQUEST MODEL
# =========================================================

class Query(BaseModel):

    query: str

    latitude: float | None = None

    longitude: float | None = None

    city: str | None = None

    state: str | None = None

    mobile: str | None = None

    image_url: str | None = None

    audio_url: str | None = None

    device_id: str | None = None


# =========================================================
# HOME
# =========================================================

@app.get("/")
async def home():

    return {
        "message": "JanSeva AI Running Successfully",
        "status": "ONLINE"
    }


# =========================================================
# DEPARTMENT KEYWORDS
# =========================================================

DEPARTMENT_KEYWORDS = {

    "Electricity": [
        "electricity",
        "electric",
        "power",
        "current",
        "light",
        "bijli",
        "bijalee",
        "voltage",
        "transformer",
        "meter",
        "wire",
        "spark",
        "blackout"
    ],

    "Water": [
        "water",
        "pani",
        "pipeline",
        "tap",
        "supply",
        "leakage",
        "leak"
    ],

    "Roads": [
        "road",
        "roads",
        "sadak",
        "pothole",
        "potholes",
        "street damage",
        "road damage"
    ],

    "Garbage": [
        "garbage",
        "waste",
        "kachra",
        "trash",
        "dirty",
        "cleanliness",
        "sanitation",
        "dump"
    ],

    "Drainage": [
        "drainage",
        "drain",
        "sewer",
        "nali",
        "sewage",
        "water logging",
        "waterlogging"
    ],

    "Health": [
        "hospital",
        "doctor",
        "medicine",
        "medical",
        "health",
        "ambulance",
        "clinic"
    ],

    "Police": [
        "police",
        "theft",
        "crime",
        "violence",
        "harassment",
        "stolen",
        "robbery"
    ],

    "Education": [
        "school",
        "college",
        "teacher",
        "education",
        "student",
        "exam"
    ],

    "Internet": [
        "internet",
        "network",
        "wifi",
        "wi-fi",
        "signal",
        "mobile network"
    ],

    "Transport": [
        "bus",
        "transport",
        "vehicle",
        "traffic",
        "public transport"
    ],

    "Agriculture": [
        "farmer",
        "agriculture",
        "crop",
        "farming",
        "fertilizer",
        "irrigation"
    ],

    "Housing": [
        "house",
        "housing",
        "property",
        "building"
    ],

    "Fire": [
        "fire",
        "aag",
        "burning",
        "smoke"
    ]
}


# =========================================================
# LOCAL DEPARTMENT DETECTION
# =========================================================

def detect_department(text):

    text = text.lower().strip()

    scores = {}

    for department, keywords in DEPARTMENT_KEYWORDS.items():

        score = 0

        for keyword in keywords:

            if keyword in text:
                score += 1

        scores[department] = score

    best_department = max(
        scores,
        key=scores.get
    )

    best_score = scores[best_department]

    if best_score == 0:
        return "Other", 55

    confidence = min(
        95,
        70 + best_score * 7
    )

    return best_department, confidence


# =========================================================
# EMERGENCY DETECTION
# =========================================================

def detect_emergency(text):

    keywords = [

        "fire",
        "murder",
        "accident",
        "rape",
        "terrorist",
        "gun",
        "attack",
        "bomb",
        "explosion",
        "kidnap",
        "ambulance",

        "aag",
        "हत्या",
        "हमला",
        "बलात्कार",
        "खून",
        "दुर्घटना"
    ]

    low = text.lower()

    return any(
        keyword.lower() in low
        for keyword in keywords
    )


# =========================================================
# FAKE DETECTION
# =========================================================

def detect_fake(text):

    low = text.lower().strip()

    # Empty input
    if not low:
        return True

    # Very short meaningless input
    if len(low) < 5:
        return True

    spam_patterns = [

        "asdf",
        "qwerty",
        "111111",
        "222222",
        "aaaaaa",
        "bbbbbb",
        "test test test",
        "spam spam spam"
    ]

    for pattern in spam_patterns:

        if pattern in low:
            return True

    # Repeated same character
    repeated = 0

    for i in range(len(low) - 1):

        if low[i] == low[i + 1]:

            repeated += 1

    if repeated >= 15:
        return True

    # Normal complaint words = NOT fake
    complaint_words = [

        "problem",
        "issue",
        "complaint",
        "not working",
        "not come",
        "not coming",
        "no supply",
        "broken",
        "damage",
        "dirty",
        "garbage",
        "electricity",
        "water",
        "road",
        "police",
        "hospital",
        "internet",
        "light",
        "power",
        "current",
        "pani",
        "bijli",
        "kachra",
        "sadak"
    ]

    if any(
        word in low
        for word in complaint_words
    ):
        return False

    return False


# =========================================================
# FIR DETECTION
# =========================================================

def detect_fir(text):

    keywords = [

        "murder",
        "rape",
        "kidnap",
        "terrorist",
        "robbery",
        "हत्या",
        "अपहरण",
        "बलात्कार"
    ]

    low = text.lower()

    return any(
        keyword.lower() in low
        for keyword in keywords
    )


# =========================================================
# CORRUPTION DETECTION
# =========================================================

def detect_corruption(text):

    words = [

        "bribe",
        "rishwat",
        "ghoos",
        "bribery",
        "घूस",
        "रिश्वत"
    ]

    low = text.lower()

    return any(
        word.lower() in low
        for word in words
    )


# =========================================================
# POLITICAL PRESSURE
# =========================================================

def detect_political_pressure(text):

    words = [

        "mla",
        "minister",
        "neta",
        "mp",
        "विधायक",
        "मंत्री",
        "नेता"
    ]

    low = text.lower()

    return any(
        word.lower() in low
        for word in words
    )


# =========================================================
# GPS
# =========================================================

def validate_gps(lat, lng):

    if lat is None or lng is None:
        return False

    if lat == 0 or lng == 0:
        return False

    return True


# =========================================================
# DUPLICATE
# =========================================================

def duplicate_complaint(query):

    low = query.lower().strip()

    for old_query in recent_complaints:

        if low == old_query.lower().strip():

            return True

    recent_complaints.append(query)

    if len(recent_complaints) > 50:

        recent_complaints.pop(0)

    return False


# =========================================================
# DEVICE RISK
# =========================================================

def device_risk(device_id):

    if device_id is None:
        return False

    count = user_behavior.get(
        device_id,
        0
    )

    user_behavior[device_id] = count + 1

    return count > 15


# =========================================================
# CLEAN JSON
# =========================================================

def clean_json(text):

    if not text:
        return ""

    text = text.strip()

    text = re.sub(
        r"```json",
        "",
        text,
        flags=re.IGNORECASE
    )

    text = re.sub(
        r"```",
        "",
        text
    )

    return text.strip()


# =========================================================
# LOCAL AI FALLBACK
# =========================================================

def local_ai_fallback(query):

    department, confidence = detect_department(
        query
    )

    is_emergency = detect_emergency(
        query
    )

    is_fake = detect_fake(
        query
    )

    if is_emergency:

        priority = "HIGH"

    else:

        priority = "NORMAL"

    return {

        "department": department,

        "message":
            f"Your complaint appears related to {department}. "
            f"Please provide more details so it can be handled properly.",

        "priority": priority,

        "summary": query[:150],

        "confidence": confidence,

        "sentiment": "Concerned",

        "detected_language": "Auto",

        "emergency": is_emergency,

        "fake_detected": is_fake,

        "complaint_intent": True,

        "needs_mobile": False,

        "suggestions": [

            "Add more details",

            "Upload evidence",

            "Provide location"

        ]

    }


# =========================================================
# GEMINI AI
# =========================================================

async def gemini_ai(query, city, state, priority):

    if model is None:

        return None

    prompt = f"""
You are JanSeva AI, an Indian citizen governance assistant.

Analyze the citizen complaint.

IMPORTANT:
- Normal citizen complaints must NOT be marked fake.
- Identify the correct department.
- Understand simple English and Indian-style English.
- Return ONLY valid JSON.
- Do not return markdown.
- Do not return explanations outside JSON.

Citizen Query:
{query}

Location:
City: {city}
State: {state}

Priority:
{priority}

Choose department from:

Water
Electricity
Roads
Garbage
Drainage
Health
Police
Education
Internet
Transport
Agriculture
Housing
Fire
Other

Return exactly this structure:

{{
    "department": "",
    "message": "",
    "priority": "",
    "summary": "",
    "confidence": 0,
    "sentiment": "",
    "detected_language": "",
    "complaint_intent": true,
    "needs_mobile": false,
    "suggestions": []
}}
"""

    try:

        response = model.generate_content(

            prompt,

            generation_config={

                "temperature": 0.2,

                "top_p": 0.9,

                "top_k": 20,

                "max_output_tokens": 1000,

                "response_mime_type":
                    "application/json"

            }

        )

        cleaned = clean_json(
            response.text
        )

        return json.loads(
            cleaned
        )

    except Exception as e:

        print(
            "Gemini Error:",
            str(e)
        )

        return None


# =========================================================
# CLASSIFY
# =========================================================

@app.post("/classify")
async def classify(

    data: Query,

    request: Request

):

    try:

        query = data.query.strip()

        ip_address = (
            request.client.host
            if request.client
            else "unknown"
        )


        # ---------------------------------------------
        # DETECTIONS
        # ---------------------------------------------

        is_emergency = detect_emergency(
            query
        )

        is_fake = detect_fake(
            query
        )

        fir_required = detect_fir(
            query
        )

        corruption = detect_corruption(
            query
        )

        political_pressure = (
            detect_political_pressure(
                query
            )
        )

        gps_valid = validate_gps(

            data.latitude,

            data.longitude

        )

        duplicate = duplicate_complaint(
            query
        )

        risky_device = device_risk(
            data.device_id
        )


        # ---------------------------------------------
        # PRIORITY
        # ---------------------------------------------

        priority = "NORMAL"

        if is_emergency:

            priority = "HIGH"

        if corruption:

            priority = "HIGH"

        if fir_required:

            priority = "CRITICAL"


        # ---------------------------------------------
        # LOCAL DEPARTMENT
        # ---------------------------------------------

        local_department, local_confidence = (
            detect_department(query)
        )


        # ---------------------------------------------
        # TRY GEMINI
        # ---------------------------------------------

        parsed_json = await gemini_ai(

            query,

            data.city,

            data.state,

            priority

        )


        # ---------------------------------------------
        # FALLBACK
        # ---------------------------------------------

        if not isinstance(
            parsed_json,
            dict
        ):

            parsed_json = local_ai_fallback(
                query
            )


        # ---------------------------------------------
        # FORCE LOCAL DEPARTMENT
        # ---------------------------------------------
        #
        # This protects simple complaints such as:
        # "my area electricity is not come"
        # "my area is very bad in garbage"
        #

        if local_department != "Other":

            parsed_json["department"] = (
                local_department
            )

            parsed_json["confidence"] = (
                local_confidence
            )


        # ---------------------------------------------
        # FORCE CORRECT FLAGS
        # ---------------------------------------------

        parsed_json["emergency"] = (
            is_emergency
        )

        parsed_json["fake_detected"] = (
            is_fake
        )

        parsed_json["fir_required"] = (
            fir_required
        )

        parsed_json["corruption_detected"] = (
            corruption
        )

        parsed_json["political_pressure"] = (
            political_pressure
        )

        parsed_json["gps_verified"] = (
            gps_valid
        )

        parsed_json["duplicate_complaint"] = (
            duplicate
        )

        parsed_json["voice_supported"] = True

        parsed_json["deepfake_check_required"] = (

            True

            if data.image_url

            else False

        )

        parsed_json["device_risk_detected"] = (
            risky_device
        )

        parsed_json["ip_address"] = (
            ip_address
        )

        parsed_json[
            "officer_corruption_suspected"
        ] = corruption


        # ---------------------------------------------
        # FAKE MESSAGE
        # ---------------------------------------------

        if is_fake:

            parsed_json["message"] = (
                "Fake or spam complaint detected."
            )


        # ---------------------------------------------
        # EMERGENCY MESSAGE
        # ---------------------------------------------

        elif is_emergency:

            original_message = (
                parsed_json.get(
                    "message",
                    "Emergency complaint detected."
                )
            )

            parsed_json["message"] = (
                "🚨 Emergency detected. "
                + str(original_message)
            )


        # ---------------------------------------------
        # NORMAL MESSAGE
        # ---------------------------------------------

        elif not parsed_json.get("message"):

            parsed_json["message"] = (

                f"Your {parsed_json.get('department', local_department)} "
                "complaint has been understood. "
                "Please provide additional details if required."

            )


        # ---------------------------------------------
        # SAVE MEMORY
        # ---------------------------------------------

        chat_history.append({

            "query": query,

            "time":
                str(datetime.now())

        })


        if len(chat_history) > 100:

            chat_history.pop(0)


        # ---------------------------------------------
        # FINAL RESPONSE
        # ---------------------------------------------

        return {

            "success": True,

            "query": query,

            "answer": parsed_json

        }


    except Exception as e:

        print(
            "CLASSIFY ERROR:",
            str(e)
        )

        return {

            "success": False,

            "error": str(e)

        }


# =========================================================
# START SERVER
# =========================================================

if __name__ == "__main__":

    uvicorn.run(

        "ai_server:app",

        host="0.0.0.0",

        port=8000,

        reload=True

    )