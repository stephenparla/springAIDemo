# 🤖 Interactive Portfolio -> Chatbot
A high-performance, cloud-native chatbot architected with a **Sidecar Mesh** and **Serverless Analytics**.

This project demonstrates a professional-grade "SDE 3" approach to cloud architecture, balancing complex system design with cost-optimized AWS infrastructure.

---

## 🏗️ System Architecture
The application follows a decoupled, microservices-oriented approach managed via the **AWS Copilot CLI**.

### Key Components:
*   **Primary Service:** Spring Boot (Java) running on **Amazon ECS Fargate**.
*   **Edge Gateway:** **Envoy Proxy** sidecar managing all ingress/egress traffic.
*   **Serverless Analytics:** Python **AWS Lambda** microservice for out-of-band visitor tracking.
*   **Data Store:** **Amazon DynamoDB** utilizing Condition Expressions for idempotent tracking.
*   **Global Delivery:** **Amazon CloudFront** for global speed and automated SSL/HTTPS management.

---

## 🛠️ The "Secret Sauce": Envoy Lua Filter
To maintain high performance and decouple analytics from business logic, I implemented a custom **Lua filter** in Envoy.

### How it works:
1.  **IP Identification:** The filter parses `CloudFront-Viewer-Address` and `X-Forwarded-For` headers to accurately identify client IPs at the edge.
2.  **Asynchronous Analytics:** On a successful `/ping` response (HTTP 200), Envoy triggers an out-of-band `httpCall` to the Lambda Analytics endpoint. This ensures the user experience is never delayed by analytics processing.
3.  **Deduplication at the Edge:** The proxy injects security-focused cookies (`HttpOnly`, `SameSite=Lax`) to prevent re-counting visitors within a 24-hour window, reducing unnecessary database writes.

---

## ⚙️ Infrastructure as Code (IaC) & DevOps
The entire stack is declarative, utilizing **AWS Copilot** and custom **CloudFormation Addons**.

*   **Cost-Optimized Networking:** Configured with **Public Subnets** to avoid $32/mo NAT Gateway fees while maintaining security via Load Balancers and CloudFront.
*   **Secure Secret Management:** Zero hardcoded credentials. All API keys (e.g., Anthropic) are securely injected via **AWS SSM Parameter Store**.
*   **Resilient Deployments:** Fine-tuned `grace_period` (300s) and health check intervals (`/actuator/health`) to accommodate the Spring Boot startup curve, ensuring zero-downtime deployments.
*   **Observability:** Integrated **CloudWatch Container Insights** for granular CPU/Memory monitoring.

---

## 🚀 Deployment & Local Development

### Prerequisites
*   AWS Copilot CLI
*   Docker

### Quick Start
```bash
# Deploy the infrastructure
copilot env init --name prod
copilot deploy --name chatbot --env prod
```

### Analytics Endpoint
The analytics microservice is deployed as a **Lambda Function URL** with strict CORS policies whitelisting `://bubble-sort.com`.

---

## 📈 Lessons Learned
*   **Container Coordination:** Balancing resource allocation (CPU/RAM) between a Java Virtual Machine and an Envoy sidecar to prevent OOM (Out of Memory) kills during high startup loads.
*   **TLS Termination:** Configuring SNI and trusted CA certificates within Envoy to communicate securely with Lambda's HTTPS endpoints.

---

**Developed by [Your Name]**  
🔗 [Live Demo](https://www.://bubble-sort.com) | [LinkedIn](Your-LinkedIn-URL)
