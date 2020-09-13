def parse_count(whole_line):
	return int(line.split("count=")[1].split(" ")[0])


def parse_millis(whole_line):
	return float(line.split("millis_diff=")[1])


lines = []

while True:
	line = input().strip("\n").strip(" ")
	if len(line) == 0:
		break
	lines.append(line)

line_idx = 0

businesses_count = []
businesses_millis = []

users_count = []
users_millis = []

reviews_count = []
reviews_millis = []

for line in lines:
	if line.startswith("business"):
		businesses_count.append(parse_count(line))
		businesses_millis.append(parse_millis(line))
	elif line.startswith("user"):
		users_count.append(parse_count(line))
		users_millis.append(parse_millis(line))
	elif line.startswith("review"):
		reviews_count.append(parse_count(line))
		reviews_millis.append(parse_millis(line))

print(f"business count={sum(businesses_count)} millis_diff={sum(businesses_millis)} (in {len(businesses_count)} times)")
print(f"user count={sum(users_count)} millis_diff={sum(users_millis)} (in {len(users_count)} times)")
print(f"review count={sum(reviews_count)} millis_diff={sum(reviews_millis)} (in {len(reviews_count)} times)")
