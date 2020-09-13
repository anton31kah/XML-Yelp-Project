from datetime import datetime


def parse_time(string):
	if '.' in string:
		return datetime.strptime(string, "%Y-%m-%dT%H:%M:%S.%fZ")
	else:
		return datetime.strptime(string, "%Y-%m-%dT%H:%M:%SZ")


files = [
	"./04-inserting-model1-redis/executor.stdout.txt",
	"./05-inserting-model2-redis/executor.stdout.txt",
	"./06-inserting-model1-memcached/executor.stdout.txt",
	"./06-inserting-model2-memcached/executor.stdout.txt"
]

for file_name in files:
	with open(file_name) as file:
		lines = file.readlines()
		line_idx = 0

		print(file_name)

		while line_idx < len(lines):
			if "count" in lines[line_idx]:
				before = lines[line_idx - 1].strip("\n")
				after = lines[line_idx].strip("\n")

				time_before = before.split(" ")[-1]
				time_after = after.split(" ")[-1]

				difference = parse_time(time_after) - parse_time(time_before)
				millis_diff = difference.total_seconds() * 1000

				count = int(after.split("count = ")[1].split(")")[0])
				name = after.split("after ")[1].split(" foreach")[0]

				if count > 0:
					print(f"    {name} {count=} {millis_diff=}")

			line_idx += 1