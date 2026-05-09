import boto3
import os
import json
from datetime import datetime

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table(os.environ['TABLE_NAME'])

def handler(event, context):
    method = event.get('requestContext', {}).get('http', {}).get('method', 'POST')
    print(f"DEBUG: Received {method} request")
    count = response.get('Count', 0)
    print(f"DEBUG: GET request - Current count is {count}")

    if method == 'GET':
        # UI LOGIC: Return total count
        response = table.scan(Select='COUNT')
        return {
            'statusCode': 200,
            'headers': {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            },
            'body': json.dumps({'total_users': count})
        }

    else:
        # ENVOY LOGIC: Record unique visit
        try:
            body = json.loads(event.get('body', '{}'))
            user_ip = body.get('ip', 'unknown_ip')
            today = datetime.utcnow().strftime('%Y-%m-%d')
            print(f"DEBUG: POST attempt for IP {user_ip} on {today}")

            table.put_item(
                Item={'userId': user_ip, 'loginDate': today},
                ConditionExpression='attribute_not_exists(userId) AND attribute_not_exists(loginDate)'
            )
            print("DEBUG: Successfully logged new unique user")
            return {'statusCode': 200, 'body': json.dumps({'status': 'logged'})}
        except Exception:
            print(f"DEBUG: ERROR during POST: {str(e)}")
            return {'statusCode': 200, 'body': json.dumps({'status': 'skipped'})}
