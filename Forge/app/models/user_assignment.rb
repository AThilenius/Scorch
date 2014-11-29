class UserAssignment
  include ApplicationHelper

  #=UserAssignment
  # UserAssignment:<CU ID>_<assignment name>:authToken

  @@classTypeName = 'UserAssignment'

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
    redis_accessor @@classTypeName + ':' + key + ':', :authToken
  end

  def delete
    # simply remove it from the set
    $redis.srem(@@classTypeName + '_meta:all', @key)
  end

  def key
    return @key
  end

  #=UserLevel Handling
  # UserLevel:<CU ID><assignment name><level name>:isCompleted
  def getUserLevel(userKey, levelKey)
    userLevel = UserLevel.get("#{userKey}#{@key}#{levelKey}")

    if userLevel.nil?
      # Create one if it doesn't exist.
      userLevel = UserLevel.create("#{userKey}#{@key}#{levelKey}")
      userLevel.isCompleted = 'false'
    end

    return userLevel
  end

end