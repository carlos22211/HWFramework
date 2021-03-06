package javax.crypto;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import sun.security.util.Debug;
import sun.util.logging.PlatformLogger;

public class Cipher {
    private static final /* synthetic */ int[] -javax-crypto-Cipher$InitTypeSwitchesValues = null;
    private static final String ATTRIBUTE_MODES = "SupportedModes";
    private static final String ATTRIBUTE_PADDINGS = "SupportedPaddings";
    public static final int DECRYPT_MODE = 2;
    public static final int ENCRYPT_MODE = 1;
    private static final String KEY_USAGE_EXTENSION_OID = "2.5.29.15";
    public static final int PRIVATE_KEY = 2;
    public static final int PUBLIC_KEY = 1;
    public static final int SECRET_KEY = 3;
    public static final int UNWRAP_MODE = 4;
    public static final int WRAP_MODE = 3;
    private static final Debug debug = null;
    private ExemptionMechanism exmech;
    private boolean initialized;
    private int opmode;
    private Provider provider;
    private CipherSpi spi;
    private final SpiAndProviderUpdater spiAndProviderUpdater;
    private final String[] tokenizedTransformation;
    private final String transformation;

    static class CipherSpiAndProvider {
        CipherSpi cipherSpi;
        Provider provider;

        CipherSpiAndProvider(CipherSpi cipherSpi, Provider provider) {
            this.cipherSpi = cipherSpi;
            this.provider = provider;
        }
    }

    static class InitParams {
        final InitType initType;
        final Key key;
        final int opmode;
        final AlgorithmParameters params;
        final SecureRandom random;
        final AlgorithmParameterSpec spec;

        InitParams(InitType initType, int opmode, Key key, SecureRandom random, AlgorithmParameterSpec spec, AlgorithmParameters params) {
            this.initType = initType;
            this.opmode = opmode;
            this.key = key;
            this.random = random;
            this.spec = spec;
            this.params = params;
        }
    }

    enum InitType {
        ;

        static {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: javax.crypto.Cipher.InitType.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:263)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: javax.crypto.Cipher.InitType.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 8 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 9 more
*/
            /*
            // Can't load method instructions.
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.crypto.Cipher.InitType.<clinit>():void");
        }
    }

    enum NeedToSet {
        ;

        static {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: javax.crypto.Cipher.NeedToSet.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:263)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: javax.crypto.Cipher.NeedToSet.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 8 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 9 more
*/
            /*
            // Can't load method instructions.
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.crypto.Cipher.NeedToSet.<clinit>():void");
        }
    }

    class SpiAndProviderUpdater {
        private final Object initSpiLock;
        private final Provider specifiedProvider;
        private final CipherSpi specifiedSpi;
        final /* synthetic */ Cipher this$0;

        SpiAndProviderUpdater(Cipher this$0, Provider specifiedProvider, CipherSpi specifiedSpi) {
            this.this$0 = this$0;
            this.initSpiLock = new Object();
            this.specifiedProvider = specifiedProvider;
            this.specifiedSpi = specifiedSpi;
        }

        void setCipherSpiImplAndProvider(CipherSpi cipherSpi, Provider provider) {
            this.this$0.spi = cipherSpi;
            this.this$0.provider = provider;
        }

        CipherSpiAndProvider updateAndGetSpiAndProvider(InitParams initParams, CipherSpi spiImpl, Provider provider) throws InvalidKeyException, InvalidAlgorithmParameterException {
            if (this.specifiedSpi != null) {
                return new CipherSpiAndProvider(this.specifiedSpi, provider);
            }
            synchronized (this.initSpiLock) {
                if (spiImpl == null || initParams != null) {
                    CipherSpiAndProvider sap = Cipher.tryCombinations(initParams, this.specifiedProvider, this.this$0.tokenizedTransformation);
                    if (sap == null) {
                        throw new ProviderException("No provider found for " + Arrays.toString(this.this$0.tokenizedTransformation));
                    }
                    setCipherSpiImplAndProvider(sap.cipherSpi, sap.provider);
                    CipherSpiAndProvider cipherSpiAndProvider = new CipherSpiAndProvider(sap.cipherSpi, sap.provider);
                    return cipherSpiAndProvider;
                }
                cipherSpiAndProvider = new CipherSpiAndProvider(spiImpl, provider);
                return cipherSpiAndProvider;
            }
        }

        CipherSpiAndProvider updateAndGetSpiAndProvider(CipherSpi spiImpl, Provider provider) {
            try {
                return updateAndGetSpiAndProvider(null, spiImpl, provider);
            } catch (GeneralSecurityException e) {
                throw new ProviderException("Exception thrown when params == null", e);
            }
        }

        CipherSpi getCurrentSpi(CipherSpi spiImpl) {
            if (this.specifiedSpi != null) {
                return this.specifiedSpi;
            }
            synchronized (this.initSpiLock) {
            }
            return spiImpl;
        }
    }

    static class Transform {
        private final String name;
        private final NeedToSet needToSet;

        public Transform(String name, NeedToSet needToSet) {
            this.name = name;
            this.needToSet = needToSet;
        }
    }

    private static /* synthetic */ int[] -getjavax-crypto-Cipher$InitTypeSwitchesValues() {
        if (-javax-crypto-Cipher$InitTypeSwitchesValues != null) {
            return -javax-crypto-Cipher$InitTypeSwitchesValues;
        }
        int[] iArr = new int[InitType.values().length];
        try {
            iArr[InitType.ALGORITHM_PARAMS.ordinal()] = PUBLIC_KEY;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[InitType.ALGORITHM_PARAM_SPEC.ordinal()] = PRIVATE_KEY;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[InitType.KEY.ordinal()] = WRAP_MODE;
        } catch (NoSuchFieldError e3) {
        }
        -javax-crypto-Cipher$InitTypeSwitchesValues = iArr;
        return iArr;
    }

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: javax.crypto.Cipher.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: javax.crypto.Cipher.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 7 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 8 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.crypto.Cipher.<clinit>():void");
    }

    protected Cipher(CipherSpi cipherSpi, Provider provider, String transformation) {
        this.initialized = false;
        this.opmode = 0;
        if (cipherSpi == null) {
            throw new NullPointerException("cipherSpi == null");
        } else if ((cipherSpi instanceof NullCipherSpi) || provider != null) {
            this.spi = cipherSpi;
            this.provider = provider;
            this.transformation = transformation;
            this.tokenizedTransformation = null;
            this.spiAndProviderUpdater = new SpiAndProviderUpdater(this, provider, cipherSpi);
        } else {
            throw new NullPointerException("provider == null");
        }
    }

    private Cipher(CipherSpi cipherSpi, Provider provider, String transformation, String[] tokenizedTransformation) {
        this.initialized = false;
        this.opmode = 0;
        this.spi = cipherSpi;
        this.provider = provider;
        this.transformation = transformation;
        this.tokenizedTransformation = tokenizedTransformation;
        this.spiAndProviderUpdater = new SpiAndProviderUpdater(this, provider, cipherSpi);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String[] tokenizeTransformation(String transformation) throws NoSuchAlgorithmException {
        if (transformation == null || transformation.isEmpty()) {
            throw new NoSuchAlgorithmException("No transformation given");
        }
        String[] parts = new String[WRAP_MODE];
        StringTokenizer parser = new StringTokenizer(transformation, "/");
        int count = 0;
        while (parser.hasMoreTokens() && count < WRAP_MODE) {
            try {
                int count2 = count + PUBLIC_KEY;
                try {
                    parts[count] = parser.nextToken().trim();
                    count = count2;
                } catch (NoSuchElementException e) {
                }
            } catch (NoSuchElementException e2) {
                count2 = count;
            }
        }
        if (!(count == 0 || count == PRIVATE_KEY)) {
            if (!parser.hasMoreTokens()) {
                if (parts[0] != null && parts[0].length() != 0) {
                    return parts;
                }
                throw new NoSuchAlgorithmException("Invalid transformation:algorithm not specified-" + transformation);
            }
        }
        throw new NoSuchAlgorithmException("Invalid transformation format:" + transformation);
    }

    public static final Cipher getInstance(String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return createCipher(transformation, null);
    }

    public static final Cipher getInstance(String transformation, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        if (provider == null || provider.length() == 0) {
            throw new IllegalArgumentException("Missing provider");
        }
        Provider p = Security.getProvider(provider);
        if (p != null) {
            return createCipher(transformation, p);
        }
        throw new NoSuchProviderException("No such provider: " + provider);
    }

    public static final Cipher getInstance(String transformation, Provider provider) throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (provider != null) {
            return createCipher(transformation, provider);
        }
        throw new IllegalArgumentException("Missing provider");
    }

    static final Cipher createCipher(String transformation, Provider provider) throws NoSuchAlgorithmException, NoSuchPaddingException {
        String[] tokenizedTransformation = tokenizeTransformation(transformation);
        try {
            if (tryCombinations(null, provider, tokenizedTransformation) != null) {
                return new Cipher(null, provider, transformation, tokenizedTransformation);
            }
            if (provider == null) {
                throw new NoSuchAlgorithmException("No provider found for " + transformation);
            }
            throw new NoSuchAlgorithmException("Provider " + provider.getName() + " does not provide " + transformation);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Key/Algorithm excepton despite not passing one", e);
        }
    }

    void updateProviderIfNeeded() {
        try {
            this.spiAndProviderUpdater.updateAndGetSpiAndProvider(null, this.spi, this.provider);
        } catch (Exception lastException) {
            ProviderException e = new ProviderException("Could not construct CipherSpi instance");
            if (lastException != null) {
                e.initCause(lastException);
            }
            throw e;
        }
    }

    private void chooseProvider(InitType initType, int opmode, Key key, AlgorithmParameterSpec paramSpec, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            this.spiAndProviderUpdater.updateAndGetSpiAndProvider(new InitParams(initType, opmode, key, random, paramSpec, params), this.spi, this.provider);
        } catch (Exception lastException) {
            if (lastException instanceof InvalidKeyException) {
                throw ((InvalidKeyException) lastException);
            } else if (lastException instanceof InvalidAlgorithmParameterException) {
                throw ((InvalidAlgorithmParameterException) lastException);
            } else if (lastException instanceof RuntimeException) {
                throw ((RuntimeException) lastException);
            } else {
                throw new InvalidKeyException("No installed provider supports this key: " + (key != null ? key.getClass().getName() : "(null)"), lastException);
            }
        }
    }

    public final Provider getProvider() {
        updateProviderIfNeeded();
        return this.provider;
    }

    public final String getAlgorithm() {
        return this.transformation;
    }

    public final int getBlockSize() {
        updateProviderIfNeeded();
        return this.spi.engineGetBlockSize();
    }

    public final int getOutputSize(int inputLen) {
        if (!this.initialized && !(this instanceof NullCipher)) {
            throw new IllegalStateException("Cipher not initialized");
        } else if (inputLen < 0) {
            throw new IllegalArgumentException("Input size must be equal to or greater than zero");
        } else {
            updateProviderIfNeeded();
            return this.spi.engineGetOutputSize(inputLen);
        }
    }

    public final byte[] getIV() {
        updateProviderIfNeeded();
        return this.spi.engineGetIV();
    }

    public final AlgorithmParameters getParameters() {
        updateProviderIfNeeded();
        return this.spi.engineGetParameters();
    }

    public final ExemptionMechanism getExemptionMechanism() {
        updateProviderIfNeeded();
        return this.exmech;
    }

    private static void checkOpmode(int opmode) {
        if (opmode < PUBLIC_KEY || opmode > UNWRAP_MODE) {
            throw new InvalidParameterException("Invalid operation mode");
        }
    }

    public final void init(int opmode, Key key) throws InvalidKeyException {
        init(opmode, key, JceSecurity.RANDOM);
    }

    public final void init(int opmode, Key key, SecureRandom random) throws InvalidKeyException {
        this.initialized = false;
        checkOpmode(opmode);
        try {
            chooseProvider(InitType.KEY, opmode, key, null, null, random);
            this.initialized = true;
            this.opmode = opmode;
        } catch (Throwable e) {
            throw new InvalidKeyException(e);
        }
    }

    public final void init(int opmode, Key key, AlgorithmParameterSpec params) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(opmode, key, params, JceSecurity.RANDOM);
    }

    public final void init(int opmode, Key key, AlgorithmParameterSpec params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initialized = false;
        checkOpmode(opmode);
        chooseProvider(InitType.ALGORITHM_PARAM_SPEC, opmode, key, params, null, random);
        this.initialized = true;
        this.opmode = opmode;
    }

    public final void init(int opmode, Key key, AlgorithmParameters params) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(opmode, key, params, JceSecurity.RANDOM);
    }

    public final void init(int opmode, Key key, AlgorithmParameters params, SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initialized = false;
        checkOpmode(opmode);
        chooseProvider(InitType.ALGORITHM_PARAMS, opmode, key, null, params, random);
        this.initialized = true;
        this.opmode = opmode;
    }

    public final void init(int opmode, Certificate certificate) throws InvalidKeyException {
        init(opmode, certificate, JceSecurity.RANDOM);
    }

    public final void init(int opmode, Certificate certificate, SecureRandom random) throws InvalidKeyException {
        this.initialized = false;
        checkOpmode(opmode);
        if (certificate instanceof X509Certificate) {
            X509Certificate cert = (X509Certificate) certificate;
            Set critSet = cert.getCriticalExtensionOIDs();
            if (!(critSet == null || critSet.isEmpty() || !critSet.contains(KEY_USAGE_EXTENSION_OID))) {
                boolean[] keyUsageInfo = cert.getKeyUsage();
                if (keyUsageInfo != null) {
                    if (opmode != PUBLIC_KEY || keyUsageInfo.length <= WRAP_MODE || keyUsageInfo[WRAP_MODE]) {
                        if (opmode == WRAP_MODE && keyUsageInfo.length > PRIVATE_KEY && !keyUsageInfo[PRIVATE_KEY]) {
                        }
                    }
                    throw new InvalidKeyException("Wrong key usage");
                }
            }
        }
        try {
            chooseProvider(InitType.KEY, opmode, certificate == null ? null : certificate.getPublicKey(), null, null, random);
            this.initialized = true;
            this.opmode = opmode;
        } catch (Throwable e) {
            throw new InvalidKeyException(e);
        }
    }

    private void checkCipherState() {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            } else if (this.opmode != PUBLIC_KEY && this.opmode != PRIVATE_KEY) {
                throw new IllegalStateException("Cipher not initialized for encryption/decryption");
            }
        }
    }

    public final byte[] update(byte[] input) {
        checkCipherState();
        if (input == null) {
            throw new IllegalArgumentException("Null input buffer");
        }
        updateProviderIfNeeded();
        if (input.length == 0) {
            return null;
        }
        return this.spi.engineUpdate(input, 0, input.length);
    }

    public final byte[] update(byte[] input, int inputOffset, int inputLen) {
        checkCipherState();
        if (input == null || inputOffset < 0 || inputLen > input.length - inputOffset || inputLen < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (inputLen == 0) {
            return null;
        }
        return this.spi.engineUpdate(input, inputOffset, inputLen);
    }

    public final int update(byte[] input, int inputOffset, int inputLen, byte[] output) throws ShortBufferException {
        checkCipherState();
        if (input == null || inputOffset < 0 || inputLen > input.length - inputOffset || inputLen < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (inputLen == 0) {
            return 0;
        }
        return this.spi.engineUpdate(input, inputOffset, inputLen, output, 0);
    }

    public final int update(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException {
        checkCipherState();
        if (input == null || inputOffset < 0 || inputLen > input.length - inputOffset || inputLen < 0 || outputOffset < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (inputLen == 0) {
            return 0;
        }
        return this.spi.engineUpdate(input, inputOffset, inputLen, output, outputOffset);
    }

    public final int update(ByteBuffer input, ByteBuffer output) throws ShortBufferException {
        checkCipherState();
        if (input == null || output == null) {
            throw new IllegalArgumentException("Buffers must not be null");
        } else if (input == output) {
            throw new IllegalArgumentException("Input and output buffers must not be the same object, consider using buffer.duplicate()");
        } else if (output.isReadOnly()) {
            throw new ReadOnlyBufferException();
        } else {
            updateProviderIfNeeded();
            return this.spi.engineUpdate(input, output);
        }
    }

    public final byte[] doFinal() throws IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(null, 0, 0);
    }

    public final int doFinal(byte[] output, int outputOffset) throws IllegalBlockSizeException, ShortBufferException, BadPaddingException {
        checkCipherState();
        if (output == null || outputOffset < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(null, 0, 0, output, outputOffset);
    }

    public final byte[] doFinal(byte[] input) throws IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (input == null) {
            throw new IllegalArgumentException("Null input buffer");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(input, 0, input.length);
    }

    public final byte[] doFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (input == null || inputOffset < 0 || inputLen > input.length - inputOffset || inputLen < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(input, inputOffset, inputLen);
    }

    public final int doFinal(byte[] input, int inputOffset, int inputLen, byte[] output) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (input == null || inputOffset < 0 || inputLen > input.length - inputOffset || inputLen < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(input, inputOffset, inputLen, output, 0);
    }

    public final int doFinal(byte[] input, int inputOffset, int inputLen, byte[] output, int outputOffset) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (input == null || inputOffset < 0 || inputLen > input.length - inputOffset || inputLen < 0 || outputOffset < 0) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        return this.spi.engineDoFinal(input, inputOffset, inputLen, output, outputOffset);
    }

    public final int doFinal(ByteBuffer input, ByteBuffer output) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        checkCipherState();
        if (input == null || output == null) {
            throw new IllegalArgumentException("Buffers must not be null");
        } else if (input == output) {
            throw new IllegalArgumentException("Input and output buffers must not be the same object, consider using buffer.duplicate()");
        } else if (output.isReadOnly()) {
            throw new ReadOnlyBufferException();
        } else {
            updateProviderIfNeeded();
            return this.spi.engineDoFinal(input, output);
        }
    }

    public final byte[] wrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            } else if (this.opmode != WRAP_MODE) {
                throw new IllegalStateException("Cipher not initialized for wrapping keys");
            }
        }
        updateProviderIfNeeded();
        return this.spi.engineWrap(key);
    }

    public final Key unwrap(byte[] wrappedKey, String wrappedKeyAlgorithm, int wrappedKeyType) throws InvalidKeyException, NoSuchAlgorithmException {
        if (!(this instanceof NullCipher)) {
            if (!this.initialized) {
                throw new IllegalStateException("Cipher not initialized");
            } else if (this.opmode != UNWRAP_MODE) {
                throw new IllegalStateException("Cipher not initialized for unwrapping keys");
            }
        }
        if (wrappedKeyType == WRAP_MODE || wrappedKeyType == PRIVATE_KEY || wrappedKeyType == PUBLIC_KEY) {
            updateProviderIfNeeded();
            return this.spi.engineUnwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
        }
        throw new InvalidParameterException("Invalid key type");
    }

    private AlgorithmParameterSpec getAlgorithmParameterSpec(AlgorithmParameters params) throws InvalidParameterSpecException {
        if (params == null) {
            return null;
        }
        String alg = params.getAlgorithm().toUpperCase(Locale.ENGLISH);
        if (alg.equalsIgnoreCase("RC2")) {
            return params.getParameterSpec(RC2ParameterSpec.class);
        }
        if (alg.equalsIgnoreCase("RC5")) {
            return params.getParameterSpec(RC5ParameterSpec.class);
        }
        if (alg.startsWith("PBE")) {
            return params.getParameterSpec(PBEParameterSpec.class);
        }
        if (alg.startsWith("DES")) {
            return params.getParameterSpec(IvParameterSpec.class);
        }
        return null;
    }

    public static final int getMaxAllowedKeyLength(String transformation) throws NoSuchAlgorithmException {
        if (transformation == null) {
            throw new NullPointerException("transformation == null");
        }
        tokenizeTransformation(transformation);
        return PlatformLogger.OFF;
    }

    public static final AlgorithmParameterSpec getMaxAllowedParameterSpec(String transformation) throws NoSuchAlgorithmException {
        if (transformation == null) {
            throw new NullPointerException("transformation == null");
        }
        tokenizeTransformation(transformation);
        return null;
    }

    public final void updateAAD(byte[] src) {
        if (src == null) {
            throw new IllegalArgumentException("src buffer is null");
        }
        updateAAD(src, 0, src.length);
    }

    public final void updateAAD(byte[] src, int offset, int len) {
        checkCipherState();
        if (src == null || offset < 0 || len < 0 || len + offset > src.length) {
            throw new IllegalArgumentException("Bad arguments");
        }
        updateProviderIfNeeded();
        if (len != 0) {
            this.spi.engineUpdateAAD(src, offset, len);
        }
    }

    public final void updateAAD(ByteBuffer src) {
        checkCipherState();
        if (src == null) {
            throw new IllegalArgumentException("src ByteBuffer is null");
        }
        updateProviderIfNeeded();
        if (src.remaining() != 0) {
            this.spi.engineUpdateAAD(src);
        }
    }

    public CipherSpi getCurrentSpi() {
        return this.spi;
    }

    static boolean matchAttribute(Service service, String attr, String value) {
        if (value == null) {
            return true;
        }
        String pattern = service.getAttribute(attr);
        if (pattern == null) {
            return true;
        }
        return value.toUpperCase(Locale.US).matches(pattern.toUpperCase(Locale.US));
    }

    static CipherSpiAndProvider tryCombinations(InitParams initParams, Provider provider, String[] tokenizedTransformation) throws InvalidKeyException, InvalidAlgorithmParameterException {
        ArrayList<Transform> transforms = new ArrayList();
        if (!(tokenizedTransformation[PUBLIC_KEY] == null || tokenizedTransformation[PRIVATE_KEY] == null)) {
            transforms.add(new Transform(tokenizedTransformation[0] + "/" + tokenizedTransformation[PUBLIC_KEY] + "/" + tokenizedTransformation[PRIVATE_KEY], NeedToSet.NONE));
        }
        if (tokenizedTransformation[PUBLIC_KEY] != null) {
            transforms.add(new Transform(tokenizedTransformation[0] + "/" + tokenizedTransformation[PUBLIC_KEY], NeedToSet.PADDING));
        }
        if (tokenizedTransformation[PRIVATE_KEY] != null) {
            transforms.add(new Transform(tokenizedTransformation[0] + "//" + tokenizedTransformation[PRIVATE_KEY], NeedToSet.MODE));
        }
        transforms.add(new Transform(tokenizedTransformation[0], NeedToSet.BOTH));
        Throwable th = null;
        Service service;
        if (provider != null) {
            for (Transform transform : transforms) {
                service = provider.getService("Cipher", transform.name);
                if (service != null) {
                    return tryTransformWithProvider(initParams, tokenizedTransformation, transform.needToSet, service);
                }
            }
        }
        Provider[] providers = Security.getProviders();
        int length = providers.length;
        for (int i = 0; i < length; i += PUBLIC_KEY) {
            Provider prov = providers[i];
            for (Transform transform2 : transforms) {
                service = prov.getService("Cipher", transform2.name);
                if (service != null) {
                    if (!(initParams == null || initParams.key == null)) {
                        if (!service.supportsParameter(initParams.key)) {
                            continue;
                        }
                    }
                    try {
                        CipherSpiAndProvider sap = tryTransformWithProvider(initParams, tokenizedTransformation, transform2.needToSet, service);
                        if (sap != null) {
                            return sap;
                        }
                    } catch (Throwable e) {
                        if (th == null) {
                            th = e;
                        }
                    }
                }
            }
        }
        if (th instanceof InvalidKeyException) {
            throw ((InvalidKeyException) th);
        } else if (th instanceof InvalidAlgorithmParameterException) {
            throw ((InvalidAlgorithmParameterException) th);
        } else if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        } else if (th != null) {
            throw new InvalidKeyException("No provider can be initialized with given key", th);
        } else if (initParams == null || initParams.key == null) {
            return null;
        } else {
            throw new InvalidKeyException("No provider offers " + Arrays.toString((Object[]) tokenizedTransformation) + " for " + initParams.key.getAlgorithm() + " key of class " + initParams.key.getClass().getName() + " and export format " + initParams.key.getFormat());
        }
    }

    static CipherSpiAndProvider tryTransformWithProvider(InitParams initParams, String[] tokenizedTransformation, NeedToSet type, Service service) throws InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            if (!matchAttribute(service, ATTRIBUTE_MODES, tokenizedTransformation[PUBLIC_KEY]) || !matchAttribute(service, ATTRIBUTE_PADDINGS, tokenizedTransformation[PRIVATE_KEY])) {
                return null;
            }
            CipherSpiAndProvider sap = new CipherSpiAndProvider((CipherSpi) service.newInstance(null), service.getProvider());
            if (sap.cipherSpi == null || sap.provider == null) {
                return null;
            }
            CipherSpi spi = sap.cipherSpi;
            if ((type == NeedToSet.MODE || type == NeedToSet.BOTH) && tokenizedTransformation[PUBLIC_KEY] != null) {
                spi.engineSetMode(tokenizedTransformation[PUBLIC_KEY]);
            }
            if ((type == NeedToSet.PADDING || type == NeedToSet.BOTH) && tokenizedTransformation[PRIVATE_KEY] != null) {
                spi.engineSetPadding(tokenizedTransformation[PRIVATE_KEY]);
            }
            if (initParams != null) {
                switch (-getjavax-crypto-Cipher$InitTypeSwitchesValues()[initParams.initType.ordinal()]) {
                    case PUBLIC_KEY /*1*/:
                        spi.engineInit(initParams.opmode, initParams.key, initParams.params, initParams.random);
                        break;
                    case PRIVATE_KEY /*2*/:
                        spi.engineInit(initParams.opmode, initParams.key, initParams.spec, initParams.random);
                        break;
                    case WRAP_MODE /*3*/:
                        spi.engineInit(initParams.opmode, initParams.key, initParams.random);
                        break;
                    default:
                        throw new AssertionError((Object) "This should never be reached");
                }
            }
            return new CipherSpiAndProvider(spi, sap.provider);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (NoSuchPaddingException e2) {
            return null;
        }
    }
}
