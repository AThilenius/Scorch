class MinecraftUser

  include ApplicationHelper

  #=Minecraft User [1:1 mapping with User, in Minecraft environment]
  # MinecraftUser:<CU ID>:Allocated MC Account Username

  @@classTypeName = 'MinecraftUser'

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
    redis_accessor @@classTypeName + ':' + key + ':', :allocatedMcAccount
  end

  def delete
    # simply remove it from the set
    $redis.srem(@@classTypeName + '_meta:all', @key)
  end

  def key
    return @key
  end

end