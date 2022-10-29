import requests
import logging
import json
import argparse
import urllib3

def ask_token_for_user_from_kc():
    data={
        "username": "ccmanager",
        "password": "Admin123",
        "client_id": "qm-scorecard-app",
        "grant_type": "password",
        "client_secret": "abe6c74f-d164-4336-8134-2ed6dbc6b0f3"
    }
    return ask_token_from_kc_with_data("vm058.dev.cz.zoomint.com", data)

def ask_token_for_app_from_kc(kc_machine_address, client_id, client_secret):
    data={
        "client_id": client_id,
        "grant_type": "client_credentials",
        "client_secret": client_secret # Installation dependant!!!!
    }
    return ask_token_from_kc_with_data(kc_machine_address, data)

def ask_token_from_kc_with_data(kc_machine_address, data):
    # urllib3.disable_warnings()
    respose = requests.post("https://"+kc_machine_address+"/auth/realms/default/protocol/openid-connect/token",
    data=data,
    headers={
        "Content-Type": "application/x-www-form-urlencoded"
    },
    verify = False) 
    resp_json = respose.json()
    if respose.status_code!=200:
        logging.error(resp_json["error"])
        raise Exception("There was a problem with the retrieval of the token: " + resp_json["error"])
    token = resp_json["access_token"]
    logging.info("The retrieved token is: %s",token)
    return token

# if __name__ == "__main__":
#     logging.basicConfig(format='%(levelname)s\t%(asctime)s\t%(message)s', level=logging.INFO)
#     parser = argparse.ArgumentParser(description='Requests a new token to a keycloak machine')
#     parser.add_argument("-t","--kc_machine_address", help="The address of the kc machine to ask the token from, example 'vm084.dev.cz.zoomint.com'", default="vm084.dev.cz.zoomint.com")
#     parser.add_argument("-c","--client_id", help="The client id used to retrieve the token, for example 'admin-cli'" , default="etl-manager-app")
#     parser.add_argument("-s","--client_secret", help="The client secret, installation dependant" , required=True)
#     args = parser.parse_args()
#     logging.debug("Targeting machine " + args.kc_machine_address + " with client " + args.client_id + " using secret "+ args.client_secret)
#     token = ask_token_for_app_from_kc(args.kc_machine_address, args.client_id, args.client_secret)
#     print(token)

token = ask_token_for_user_from_kc()
logging.info(token)
