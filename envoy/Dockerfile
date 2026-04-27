FROM envoyproxy/envoy:v1.28-latest

COPY envoy.yaml /etc/envoy/envoy.yaml

USER root
# 💡 Give the envoy binary permission to bind to port 80/443 without being root
RUN apt-get update && apt-get install -y libcap2-bin && \
    setcap 'cap_net_bind_service=+ep' /usr/local/bin/envoy && \
    chmod 644 /etc/envoy/envoy.yaml

# Switch back to the non-root 'envoy' user for security
USER envoy

ENTRYPOINT ["/usr/local/bin/envoy"]
CMD ["-c", "/etc/envoy/envoy.yaml"]
