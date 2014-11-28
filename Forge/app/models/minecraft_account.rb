class MinecraftAccount

	include ApplicationHelper

	# key :username
	#  Define things like @redis_key_prefix = ...

	# field :password, :clientToken, :assignedUser

	# Defines:

	# Static:
	# 	User[] all
	# 	User get (username)
	#   User create (username)
	#   delete
	#   where (named params)

	# Key types:
	#   Fields:
	#     password: MinecraftAccount:deathsshado0:password
	#	  Meta:
	#     All:  MinecraftAccount:deathsshado0:meta:all

	key = :username
	thisClassName = :MinecraftAccount
	fields = [:password, :clientToken, :assignedUser]

	def all
		setKey = :MinecraftAccount.to_s + ':' + :username.to_s + ':meta:all'
		setData = []
		$redis.smembers(setKey).each do |key|
			setData << MinecraftAccount.getFromRedisKey(name)
		end

		return setData
	end


	def self.get(username)
		if username.nil? or username.empty?
			return nil
		end

		# Load from Redis
		jsonData = $redis.get("minecraft_account_[#{username}]")

		# DNE Check
		if jsonData.nil?or jsonData.empty?
			return nil
		end

		return MinecraftAccount.new(jsonData)
	end

	def self.getFromRedisKey(key)

	end


	def delete

	end


	private_class_method :getFromRedisKey







	attr_accessor :username, :password, :clientToken, :assignedUser

	def self.getAll
		$redis.smembers("minecraft_account_meta_all").sort.each do |name| 
			yield MinecraftAccount.get(name)
		end
	end



	def self.create(username, password)
		return MinecraftAccount.new({:username => username, :password => password}.to_json)
	end

	def initialize(json)
		dataHash = JSON.parse(json)
		@username = dataHash["username"]
		@password = dataHash["password"]
		@clientToken = dataHash["clientToken"]
		@assignedUser = dataHash["assignedUser"]
	end

	def save
		if @username.nil? or @username.empty?
			return
		end

		# Save it to meta
		$redis.sadd("minecraft_account_meta_all", @username)

		#Safe JSON
		jsonData = 
		 {:username => @username,
			:password => @password,
			:clientToken => @clientToken,
			:assignedUser => @assignedUser}.to_json

		$redis.set("minecraft_account_[#{@username}]", jsonData)
	end

end