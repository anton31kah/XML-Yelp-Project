import requests
from lxml import html


def format_url(address, port):
	return f"http://{address}:{port}/"


# ip_address = input("Address from Ljupcho >>> ")
ip_address = "15.161.217.101"
url = format(ip_address, 9090)

res = requests.get(url)

selector = "body > div > div:nth-child(3) > div > div > table > tbody > tr:nth-child > td:nth-child(1) > a"

# /html/body/div/div[3]/div/div/table/tbody/tr[1]/td[1]/a
# /html/body/div/div[3]/div/div/table/tbody/tr[2]/td[1]/a

doc = html.document_fromstring(res.content)

ports = doc.xpath("/html/body/div/div[3]/div/div/table/tbody/tr/td[1]/a")

workers = [format(ip_address, 8080 + x) for x in range(1, ports + 1)]

print(x)
