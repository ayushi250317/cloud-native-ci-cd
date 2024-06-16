# Use the official Python image as the base image
FROM python:3.12-slim

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file and install dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy the entire project directory to the container
COPY . .

# Set the environment variable for Flask
ENV FLASK_ENV=development

# Expose the port the app runs on
EXPOSE 8000

# Run the Flask application
CMD ["flask", "run", "--host=0.0.0.0","--port=8000"]