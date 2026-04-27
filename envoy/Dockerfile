FROM envoyproxy/envoy:v1.28-latest

# 1. Copy the config to the standard Envoy location
COPY envoy.yaml /etc/envoy/envoy.yaml

# 2. Force permissions so Envoy (which runs as a non-root user) can read it
USER root
RUN chmod 644 /etc/envoy/envoy.yaml
USER envoy

# 3. Use the default entrypoint but specify the config file
# This is more stable than using CMD with the full path
ENTRYPOINT ["/usr/local/bin/envoy"]
CMD ["-c", "/etc/envoy/envoy.yaml", "--service-cluster", "chatbot-proxy"]
