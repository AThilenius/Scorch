class User

	def self.getAllUsersFromMeta
		$redis.smembers("player_meta_all_players").each do |name| 
			yield User.new(name)
		end
	end

	# Constructor
	def initialize(username)
		@username = username
	end

	# User Name
	def username
		return @username
	end
	def username=(str)
		raise 'Do not change user names.'
	end

	# Display Name
	def displayName
		return $redis.get("player_[#{@username}]_displayName")
	end
	def displayName=(str)
		$redis.set("player_[#{@username}]_displayName", str)
	end

	# Password
	def password
		return $redis.get("player_[#{@username}]_password")
	end
	def password=(str)
		$redit.set("player_[#{@username}]_password", str)
	end

	# Auth Token
	def authToken
		return $redis.get("authToken_[#{@username}]_")
	end
	def authToken=(str)
		raise 'Do not change auth tokens.'
	end

	# Spawn Location
	def spawnLocation
		return $redis.get("player_[#{@username}]_spawnLocation")
	end
	def spawnLocation=(str)
		$redit.set("player_[#{@username}]_spawnLocation", str)
	end
	
end
