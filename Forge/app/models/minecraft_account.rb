class MinecraftAccount

	include ApplicationHelper

	def self.getAllMinecraftAccountsFromMeta
		$redis.smembers("minecraft_account_meta_all").each do |name| 
			yield MinecraftAccount.new(name)
		end
	end

	def self.fromUsername(username)
		if username == nil or username == ""
			return nil
		end

		# Verify existance by password
		password = $redis.get("minecraft_account_[#{username}]_password")
		if password == nil or password == ""
			return nil
		end

		return User.new(username)
	end

	def initialize(username)
		@username = username
		redis_accessor "minecraft_account_[#{username}]_", :password, :clientToken,
			:assignedUser
	end

	def save
		$redis.sadd("minecraft_account_meta_all", @username)
	end

end