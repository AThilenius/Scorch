class MinecraftAccount

	include ApplicationHelper

	#=Minecraft Account
	# MinecraftAccount_meta:all
	# MinecraftAccount:<account UUID>:username
	# MinecraftAccount:<account UUID>:password
	# MinecraftAccount:<account UUID>:clientToken
	# MinecraftAccount:<account UUID>:state [allocated/free/suspended/private]

	@@classTypeName = 'MinecraftAccount'

	def self.all
		setData = []
		$redis.smembers(@@classTypeName + '_meta:all').each do |key|
			setData << new(key)
		end

		return setData
	end

	def self.get(key)
		thisKey = @@classTypeName + '_meta:all'
		if not $redis.sismember(@@classTypeName + '_meta:all', key)
			return nil
		end

		return new(key)
	end

	def self.create(key)
		if $redis.sismember(@@classTypeName + '_meta:all', key)
			return nil
		end

		# Add it to the all list
		$redis.sadd(@@classTypeName + '_meta:all', key)
		return new(key)
	end

	def initialize(key)
		@key = key
		redis_accessor @@classTypeName + ':' + key + ':', :username, :password, :clientToken, :state
	end

	def delete
		# simply remove it from the set
		$redis.srem(@@classTypeName + '_meta:all', @key)
	end

	def key
		return @key
	end

end