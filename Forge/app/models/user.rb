class User

	include ApplicationHelper

	def self.getAllUsersFromMeta
		$redis.smembers("player_meta_all_players").each do |name| 
			yield User.new(name)
		end
	end

	def self.fromUsername(username)
		if username == nil or username == ""
			return nil
		end

		# Verify existance by display name
		displayName = $redis.get("player_[#{username}]_displayName")
		if displayName == nil or displayName == ""
			return nil
		end

		return User.new(username)
	end

	def initialize(username)
		@username = username

		redis_accessor "player_[#{username}]_", :displayName, :password, :spawnLocation,
			:authToken, :firstName, :lastName, :permissions
	end

	# User Name
	def username
		return @username
	end
	
end
