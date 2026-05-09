import boto3
import os
import json
from datetime import datetime

# Initialize the DynamoDB client outside the handler for better performance
dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table(os.environ['TABLE_NAME'])

def handler(event, context):
    # Detect if the call is from the UI (GET) or Envoy (POST)
    method = event.get('requestContext', {}).get('http', {}).get('method', 'POST')

    if method == 'GET':
        # --- UI LOGIC: Return total count ---
        response = table.scan(Select='COUNT')
        return {
            'statusCode': 200,
            'headers': {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*' # Required for browser fetch
            },
            'body': json.dumps({'total_users': response['Count']})
        }

    else:
        # --- ENVOY LOGIC: Record unique visit ---
        # (This handles POST or any other method as a 'write' attempt)
        try:
            body = json.loads(event.get('body', '{}'))
            user_ip = body.get('ip', 'unknown_ip')
            today = datetime.utcnow().strftime('%Y-%m-%d')

            table.put_item(
                Item={'userId': user_ip, 'loginDate': today},
                ConditionExpression='attribute_not_exists(userId) AND attribute_not_exists(loginDate)'
            )
            return {'statusCode': 200, 'body': json.dumps({'status': 'logged'})}
        except Exception:
            # We return 200 even on failure so Envoy doesn't freak out
            return {'statusCode': 200, 'body': json.dumps({'status': 'skipped'})}
        }
