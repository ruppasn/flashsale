package com.turvo.flashsale.service.cache;

/**
 * Pessimistic, timeout locking interface
 * Default implementation uses a Redis backend
 */
public interface ILockService {

    /**
     * Acquires timed-out distributed lock
     *
     * @param lockName
     * @param acquireTimeout in Milliseconds
     * @param lockTimeout in Milliseconds
     * @return unique identifier to the caller of this lock.
     */
    String acquireLockWithTimeout(final String lockName, final Long acquireTimeout, final Long lockTimeout);

    /**
     * releases lock, given lock identifier obtained by calling method above
     * @param lockName
     * @param identifier
     * @return {@link Boolean} indicating if lock was released
     */
    Boolean releaseLock(final String lockName, final String identifier);

}
