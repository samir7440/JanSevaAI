from transformers import pipeline

# AI Model Load
classifier = pipeline(
    "zero-shot-classification",
    model="facebook/bart-large-mnli"
)

# Dynamic Categories
categories = [
    "Electricity Problem",
    "Water Supply Issue",
    "Road Damage",
    "Garbage / Sanitation",
    "Drainage Problem",
    "Street Light Issue",
    "Hospital Service",
    "Police Complaint",
    "Public Transport Problem",
    "Pollution Issue",
    "Illegal Construction",
    "School Issue",
    "Internet / Network Problem",
    "Fire Emergency",
    "Traffic Problem"
]

# Prediction Function
def predict_category(text):

    result = classifier(
        text,
        categories
    )

    return {
        "category": result["labels"][0],
        "confidence": round(result["scores"][0] * 100, 2)
    }