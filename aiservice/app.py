from fastapi import FastAPI
from pydantic import BaseModel
from classifier import predict_category

app = FastAPI()

class Complaint(BaseModel):
    text: str

@app.post("/classify")
def classify(complaint: Complaint):

    result = predict_category(complaint.text)

    return {
        "complaint": complaint.text,
        "predicted_category": result["category"],
        "confidence": result["confidence"]
    }